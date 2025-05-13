# Serán relativamente similares a los hechos en los tutoriales, PERO claro sin la librería está de pika

from broker import Broker
import time

def procesar_mensaje(mensaje):
    print(f"Procesando mensaje: {mensaje}")

def main():
    broker = Broker()
    # Idempotente da igual redeclararla
    broker.declarar_cola("cola_principal")

    # Simular el consumo de mensajes
    broker.consumir("cola_principal", procesar_mensaje)


    while True:
        time.sleep(1)

if __name__ == "__main__":
    main()