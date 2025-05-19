import asyncio, json, websockets

async def consume(cola):
    uri = f"ws://localhost:6789/queues/{cola}/subscribe"
    while True:
        try:
            async with websockets.connect(uri) as ws:
                async for raw in ws:
                    msg = json.loads(raw)
                    mid, content = msg["id"], msg["content"]
                    print(f"📥 Recibido ({mid}): {content}")
                    # Procesamiento...
                    await ws.send(json.dumps({"ack": mid}))
        except Exception as e:
            print("Error:", e, "=> reconectando en 5s")
            await asyncio.sleep(5)

if __name__=="__main__":
    asyncio.run(consume("cola_principal"))

