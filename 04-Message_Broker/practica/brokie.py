import asyncio
import functools
import json
import sqlite3
import time
import websockets
from pathlib import Path

DB_PATH = Path("broker.db")
ACK_TIMEOUT = 60
MAX_RETRIES = 5
CLEANUP_INTERVAL = 5

# --- Inicialización de SQLite ---
def init_db():
    conn = sqlite3.connect(DB_PATH)
    c = conn.cursor()
    c.execute("""
    CREATE TABLE IF NOT EXISTS messages (
        id          INTEGER PRIMARY KEY AUTOINCREMENT,
        queue       TEXT    NOT NULL,
        content     TEXT    NOT NULL,
        expire_at   REAL    NOT NULL,
        retries     INTEGER NOT NULL DEFAULT 0,
        in_flight   INTEGER NOT NULL DEFAULT 0,
        delivered_at REAL
    )""")
    conn.commit()
    return conn

# --- Operaciones sobre la BD ---
def enqueue(conn, queue, content, ttl):
    expire = time.time() + ttl
    conn.execute(
        "INSERT INTO messages(queue,content,expire_at) VALUES (?,?,?)",
        (queue, content, expire)
    )
    conn.commit()

def get_next(conn, queue):
    now = time.time()
    row = conn.execute("""
    SELECT id,content FROM messages
     WHERE queue=? AND expire_at>? AND
       (in_flight=0 OR (in_flight=1 AND delivered_at<?))
     ORDER BY retries, id
     LIMIT 1
    """, (queue, now, now - ACK_TIMEOUT)).fetchone()
    return row

def mark_sent(conn, msg_id):
    now = time.time()
    conn.execute("""
      UPDATE messages
      SET in_flight=1, delivered_at=?, retries=retries+1
      WHERE id=?
    """, (now, msg_id))
    conn.commit()

def ack_message(conn, msg_id):
    conn.execute("DELETE FROM messages WHERE id=?", (msg_id,))
    conn.commit()

def requeue_or_dlq(conn, queue):
    dlq = f"{queue}.dlq"
    rows = conn.execute("""
      SELECT id,content,expire_at FROM messages
       WHERE queue=? AND retries>?
    """, (queue, MAX_RETRIES)).fetchall()
    for mid, content, expire in rows:
        conn.execute(
            "INSERT INTO messages(queue,content,expire_at) VALUES (?,?,?)",
            (dlq, content, expire)
        )
        conn.execute("DELETE FROM messages WHERE id=?", (mid,))
    conn.commit()

def cleanup_expired(conn):
    now = time.time()
    conn.execute("DELETE FROM messages WHERE expire_at<?", (now,))
    conn.commit()

# --- Tarea periódica ---
async def background_tasks(conn):
    while True:
        cleanup_expired(conn)
        queues = [r[0] for r in conn.execute("SELECT DISTINCT queue FROM messages")]
        for q in queues:
            requeue_or_dlq(conn, q)
        await asyncio.sleep(CLEANUP_INTERVAL)

# --- Manejador WebSocket closed-over con DB ---
async def handler(ws, path, db_conn):
    parts = path.strip("/").split("/")
    if len(parts) != 3 or parts[0] != "queues":
        await ws.close()
        return

    queue_name, action = parts[1], parts[2]

    if action == "publish":
        async for raw in ws:
            try:
                data = json.loads(raw)
                content = data["content"]
                ttl     = data.get("ttl", 300)
                enqueue(db_conn, queue_name, content, ttl)
            except:
                continue

    elif action in ("subscribe", "dlq"):
        target_queue = queue_name if action == "subscribe" else queue_name
        while True:
            nxt = get_next(db_conn, target_queue)
            if not nxt:
                await asyncio.sleep(1)
                continue

            msg_id, content = nxt
            payload = json.dumps({"id": msg_id, "content": content})
            try:
                await ws.send(payload)
                mark_sent(db_conn, msg_id)
            except:
                break

            try:
                ack = await asyncio.wait_for(ws.recv(), timeout=ACK_TIMEOUT)
                parsed = json.loads(ack)
                if parsed.get("ack") == msg_id:
                    ack_message(db_conn, msg_id)
            except asyncio.TimeoutError:
                # No ACK: quedará elegible para reprocesar
                pass
            except:
                break

    else:
        await ws.close()

async def main():
    db_conn = init_db()
    # Cerramos db_conn dentro del handler
    handler_with_db = functools.partial(handler, db_conn=db_conn)

    server = await websockets.serve(
        handler_with_db,
        "0.0.0.0", 6789,
        ping_interval=None
    )

    # Levantamos la limpieza periódica
    asyncio.create_task(background_tasks(db_conn))

    print("Broker avanzado escuchando en ws://0.0.0.0:6789")
    await server.wait_closed()

if __name__ == "__main__":
    asyncio.run(main())

