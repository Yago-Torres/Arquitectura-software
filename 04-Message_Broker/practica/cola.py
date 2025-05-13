# cola.py
import threading
from collections import deque
from mensaje import Mensaje
import time

class Cola:
    def __init__(self, max_size=1000):
        self.messages = deque(maxlen=max_size)
        self.subscribers = []
        self.lock = threading.Lock()
        self.delivered_messages = set()

    def agregar_mensaje(self, mensaje: Mensaje):
        with self.lock:
            self.messages.append(mensaje)

    def obtener_mensaje(self, ack_timeout=30):
        with self.lock:
            for msg in self.messages:
                if not msg.delivered and msg.message_id not in self.delivered_messages:
                    msg.delivered = True
                    msg.timestamp = time.time()  # Reset TTL after delivery
                    return msg
            return None

    def confirmar_entrega(self, message_id):
        with self.lock:
            self.delivered_messages.add(message_id)

    def limpiar_expirados(self):
        with self.lock:
            self.messages = deque(
                [msg for msg in self.messages if not msg.is_expired()],
                maxlen=self.messages.maxlen
            )

    def agregar_subscriptor(self, callback):
        with self.lock:
            self.subscribers.append(callback)

    def remover_subscriptor(self, callback):
        with self.lock:
            try:
                self.subscribers.remove(callback)
            except ValueError:
                pass