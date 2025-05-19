import asyncio, json, websockets

async def consume_dlq(cola):
    uri = f"ws://localhost:6789/queues/{cola}.dlq/subscribe"
    async with websockets.connect(uri) as ws:
        async for raw in ws:
            msg = json.loads(raw)
            print("🛑 DLQ:", msg["content"])

if __name__=="__main__":
    asyncio.run(consume_dlq("cola_principal"))

