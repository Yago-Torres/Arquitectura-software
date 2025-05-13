import requests
import time

BROKER_URL = "http://localhost:5000"

class Productor:
    def __init__(self, broker_url=BROKER_URL, timeout=10):
        self.broker_url = broker_url
        self.timeout = timeout
        
    def crear_cola(self, nombre_cola):
        try:
            response = requests.put(
                f"{self.broker_url}/queues/{nombre_cola}",
                timeout=self.timeout
            )
            return response.status_code == 201
        except:
            return False
            
    def publicar(self, nombre_cola, contenido, ttl=300):
        try:
            response = requests.post(
                f"{self.broker_url}/queues/{nombre_cola}/messages",
                json={"content": contenido, "ttl": ttl},
                timeout=self.timeout
            )
            return response.status_code == 202
        except:
            return False

if __name__ == "__main__":
    productor = Productor(timeout=15)
    
    if productor.crear_cola("cola_principal"):
        print("Cola creada/verificada")
        for i in range(5):
            if productor.publicar("cola_principal", f"Mensaje {i}"):
                print(f"Mensaje {i} publicado")
            else:
                print(f"Error publicando mensaje {i}")
            time.sleep(1)
    else:
        print("Error: No se pudo inicializar la cola")