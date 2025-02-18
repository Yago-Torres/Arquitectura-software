### Readme lol
hola
soy un readme
me vas a leer


#### Dependencias:

- libcurl4-openssl-dev - install with: 
~~~ 
sudo apt-get install libcurl4-openssl-dev
~~~

- nlohmann/json - hpp is added to include folder directly


[Library docs](https://curl.se/libcurl/c/)

## TODO

- Solve "dangling subjects" issue (either delete subjects or, if we allow moire than one subject, delete from the vector)
- Solve "subject state consistent", right now we call the notify before updating subject state, we will always be 1 "info" behind
- We should priorize push model, since the subject knows the observers want their info
- Add only update me of this hour's temperature feature? (punto 7)
- Add a observer observing various subjects + wait for all subjects to be updated before notifying (Change manager)
- 