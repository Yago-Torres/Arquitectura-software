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


