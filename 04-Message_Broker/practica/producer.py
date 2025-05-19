import asyncio, json, websockets, time

async def publish(queue, message, ttl=300):
    uri = f"ws://localhost:6789/queues/{queue}/publish"
    async with websockets.connect(uri) as ws:
        await ws.send(json.dumps({"content": message, "ttl": ttl}))

if __name__=="__main__":
    for i in range(5):
        asyncio.run(publish("cola_principal", f"Mensaje {i}", ttl=60))
        print("Publicado Mensaje", i)
        time.sleep(1)

