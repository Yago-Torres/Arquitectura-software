# mensaje.py
import time
import uuid
from dataclasses import dataclass

@dataclass
class Mensaje:
    content: str
    ttl: int = 300  # 5 minutes default
    message_id: str = None
    timestamp: float = None
    delivered: bool = False

    def __post_init__(self):
        self.message_id = self.message_id or uuid.uuid4().hex
        self.timestamp = self.timestamp or time.time()

    def is_expired(self):
        return (time.time() - self.timestamp) > self.ttl

    def to_dict(self):
        return {
            'id': self.message_id,
            'content': self.content,
            'timestamp': self.timestamp,
            'ttl': self.ttl
        }