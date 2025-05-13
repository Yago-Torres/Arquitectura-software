# Implementación de la cola de mensajes
# Constructor y destructor (unneded?)
# Funciones añadir a la cola, recuperar de la cola, eliminar

# Avanzado -> todo lo related con ACKS
# Avanzado -> listado y eliminación de colas
# Avanzado -> Política fair dispatch (tutorial 2, buscar)
# Avanzado -> Persistencia de mensajes, recuperación trás caida
# Avanzado -> Separación de máquinas, distribuido


# Declarar cola será IDEMPOTENTE (singleton) por lo que llamar a declarar_cola(params) con mismos params no crea más colas

# publicar(params) operacion que llama el publicador para publiar a la cola. esto estará hecho en broker no aquí, pero habrá una funcion aquí que gestione la publicación

# same shi con consumir

# publicar en una cola que NO existe descarta el mensajes

# consumer implementa un callback que MOM invoca cuando hay un mensaje en la cola

# Máximo tiempo de vida qeu pasa un mensaje en la cola es 5 minutos, trás esto se eliminar

# round robin si hay varios consumidores suscritos a la misma cola

import threading
import time
from typing import Dict 
from mensaje import Mensaje
from cola import Cola

class Broker:
    _instance = None
    _lock = threading.Lock()

    def __new__(cls):
        with cls._lock:
            if not cls._instance:
                cls._instance = super().__new__(cls)
                cls._instance.colas = {}  # type: Dict[str, 'Cola']
                cls._instance._init_cleanup()
            return cls._instance
    
    def _init_cleanup(self):
        """Start background cleanup thread"""
        def cleanup():
            while True:
                time.sleep(30)  # Clean every 30 seconds
                with self._lock:
                    for cola in self.colas.values():
                        cola.limpiar_expirados()
        
        threading.Thread(target=cleanup, daemon=True).start()
        
    def declarar_cola(self, nombre_cola: str):
        with self._lock:
            if nombre_cola not in self.colas:
                self.colas[nombre_cola] = Cola()

        
    def publicar(self, nombre_cola: str, mensaje: Mensaje):
        with self._lock:
            if nombre_cola in self.colas:
                cola = self.colas[nombre_cola]
                cola.add_message(mensaje)
                self._distribuir_mensaje(nombre_cola)
            else:
                print(f"Cola {nombre_cola} no existe. Mensaje descartado.")

    def consumir(self, nombre_cola: str, callback):
        with self._lock:
            if nombre_cola in self.colas:
                cola = self.colas[nombre_cola]
                cola.agregar_subscriptor(callback)
                self._distribuir_mensaje(nombre_cola)
            else:
                print(f"Cola {nombre_cola} no existe. No se puede consumir.")

    def _distribuir_mensaje(self, nombre_cola: str):
        cola = self.colas[nombre_cola]
        while True:
            resultado = cola.obtener_mensaje()
            if not resultado:
                break

            mensaje, consumer = resultado
            threading.Thread(target=consumer, args=(mensaje.contenido,)).start()

   

if __name__ == "__main__":
    broker = Broker()
    print("Broker running. Press Ctrl+C to stop.")
    try:
        while True:  # Keep alive forever
            time.sleep(1)
    except KeyboardInterrupt:
        print("\nBroker stopped")