import asyncio
from websockets.client import connect

BROKER_WS = "ws://localhost:5001"

async def consumir(nombre_cola):
    reconnect_delay = 1
    while True:
        try:
            async with connect(
                f"{BROKER_WS}/queues/{nombre_cola}/subscribe",
                ping_interval=20,
                ping_timeout=30
            ) as ws:
                print(f"Conectado a {nombre_cola}")
                reconnect_delay = 1
                
                async for message in ws:
                    print("Mensaje recibido:", message)
                    await ws.send("ACK")
                    
        except Exception as e:
            print(f"Error: {str(e)}. Reconectando en {reconnect_delay}s...")
            await asyncio.sleep(reconnect_delay)
            reconnect_delay = min(reconnect_delay * 2, 30)  # Exponential backoff

if __name__ == "__main__":
    asyncio.run(consumir("cola_principal"))