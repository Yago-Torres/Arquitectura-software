import threading

def Cola:

    def __init__(self):
        self.mensajes = []
        self.consumidores = []
        self.consumer_index = 0
        self.lock = threading.Lock()
    
    def agregar_mensaje(self, mensaje):
        with self.lock:
            self.mensajes.append(mensaje)

    def agregar_consumidor(self, consumidor):
        with self.lock:
            self.consumidores.append(consumidor)
            
    def obtener_mensaje(self):
        with self.lock:
            if not self.mensajes or self.consumidores:
                return None

            consumer = self.consumidores[self.consumer_index]
            self.consumer_index = (self.consumer_index + 1) % len(self.consumidores)
                