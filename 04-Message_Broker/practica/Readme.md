# Message Broker WebSocket Avanzado

Este repositorio contiene la implementación de un broker de mensajería con persistencia en SQLite, confirmaciones (ACK), reintentos y Dead-Letter Queue (DLQ), como se menciona en el enunciado para la versión avanzada.

---

## Índice

- [Estructura de ficheros](#estructura-de-ficheros)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
  - [Arrancar el broker](#arrancar-el-broker)
  - [Publicar mensajes (Producer)](#publicar-mensajes-producer)
  - [Consumir mensajes (Consumer)](#consumir-mensajes-consumer)
  - [Consumidor de Dead-Letter Queue (DLQ)](#consumidor-de-dead-letter-queue-dlq)

---

## Estructura de ficheros

- `brokie.py` : Broker avanzado.
- `producer.py`      : Script de ejemplo para publicar mensajes.
- `consumer.py`      : Script de ejemplo para consumir mensajes y enviar ACK.
- `consumer_error_login.py`  : Script para suscribirse a la cola `.dlq`. No es obligatorio pero mostrará si algún mensaje se reenvia más de MAX_RETRIES veces y no ha sido confirmado por su consumidor.

## Requisitos

- Python 3.7+
- pip

## Instalación

```bash
# 1. Clona o descarga
$ tar -xvf practica4.tar
$ cd practica

# 2. Crear y activar entorno virtual (opcional)
$ python3 -m venv venv
$ source venv/bin/activate       # Linux/macOS
# .\venv\Scripts\Activate.ps1    # Windows PowerShell

# 3. Instalar dependencias
$ pip install websockets

Uso
Arrancar el broker

$ python3 brokie.py

Verás:

Broker avanzado escuchando en ws://0.0.0.0:6789


IMPORTANTE: modificar la ip de producer y consumer para apuntar a donde esté el broker

Publicar mensajes (Producer)

$ python3 producer.py <nombre_cola> [--count N] [--ttl SEG] [--delay SEC]

    <nombre_cola>: nombre de la cola (e.g. cola_principal).

    --count, -n: número de mensajes a enviar (por defecto: 5).

    --ttl: TTL de cada mensaje en segundos (por defecto: 300).

    --delay, -d: segundos de espera entre mensajes (por defecto: 1.0).

Ejemplo:

$ python3 producer.py cola_principal --count 10 --ttl 60 --delay 0.5

Consumir mensajes (Consumer)

$ python3 consumer.py <nombre_cola>

Ejemplo:

$ python3 consumer.py cola_principal

Este cliente recibe mensajes y envía el ACK.
Consumidor de Dead-Letter Queue (DLQ)

$ python3 dlq_consumer.py <nombre_cola>

Se conecta a <nombre_cola>.dlq.

Ejemplo:

$ python3 dlq_consumer.py cola_principal


Entorno virtual

# Activar
$ source venv/bin/activate
# Desactivar
$ deactivate
