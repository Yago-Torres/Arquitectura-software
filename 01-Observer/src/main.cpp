#include "AbstractObserver.hpp"
#include "AbstractSubject.hpp"
#include "ConcreteObserver.hpp"
#include "ConcreteSubject.hpp"
#include "string"
#include <fstream>
#include <unistd.h>



// para no tener q poner el textaco cada vez
using namespace std;

const string zgzurl="https://api.open-meteo.com/v1/forecast?latitude=41.6561&longitude=-0.8773&hourly=temperature_2m&forecast_days=1";
const string tdlurl="https://api.open-meteo.com/v1/forecast?latitude=42.0744&longitude=-1.6372&hourly=temperature_2m&forecast_days=1";  



int main() {

    ConcreteSubject* zaragoza = new ConcreteSubject("zaragoza.txt", zgzurl);
    ConcreteSubject* tudela = new ConcreteSubject("tudela.txt", tdlurl);
    
    ConcreteObserver* observer_zaragoza = new ConcreteObserver(zaragoza);
    ConcreteObserver* observer2_zaragoza = new ConcreteObserver(zaragoza);
    ConcreteObserver* observer3_tudela = new ConcreteObserver(tudela);

    zaragoza->Attach(observer_zaragoza);
    zaragoza->Attach(observer2_zaragoza);
    tudela->Attach(observer3_tudela);

    while (true) {

        // Actualizar los datos de los observadores, notifica si hay nuevos a todos los observadores que tienen en su lista
        zaragoza->checkSources();
        tudela->checkSources();
        // Esperar 10 segundos aproximadamente antes de la siguiente actualización
        sleep(3600);
    }
}