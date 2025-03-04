## Readme

El programa ha sido realizado en c++. Se han empleado algunas librerías explicadas a continuación.

#### Dependencias:

- libcurl4-openssl-dev - install with: 
~~~ 
sudo apt-get install libcurl4-openssl-dev
~~~

- nlohmann/json - hpp is added to include folder directly


[Library docs](https://curl.se/libcurl/c/)

## 

El código implementado en c++ es el equivalente a una aplicación del tiempo, con diversos observadores y sujetos siguiendo el patrón observador. Esto se encuentra explicado en más profundidad en el documento pdf del zip.

Actualmente, hay dos "opciones" de ejecución, TEST y real. Esto es debido a que la api del tiempo empleada realiza una actualización cada hora, lo que es poco cómodo para ver el funcionamiento de la aplicación. Con una flag agregada en el makefile, TEST, se solventa esto creando y añadiendo en el documento de texto de registro valores simulados.
