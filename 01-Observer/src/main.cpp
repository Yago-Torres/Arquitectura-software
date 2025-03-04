#include "AbstractObserver.hpp"
#include "AbstractSubject.hpp"
#include "ConcreteObserver.hpp"
#include "ConcreteSubject.hpp"
#include "string"
#include <fstream>
#include <unistd.h>

#ifdef TEST
#include <iostream>
#endif

// para no tener q poner el textaco cada vez
using namespace std;

const string zgzurl="https://api.open-meteo.com/v1/forecast?latitude=41.6561&longitude=-0.8773&hourly=temperature_2m&forecast_days=1";
const string tdlurl="https://api.open-meteo.com/v1/forecast?latitude=42.0744&longitude=-1.6372&hourly=temperature_2m&forecast_days=1";  
const string gtcurl="https://api.open-meteo.com/v1/forecast?latitude=41.6561&longitude=-0.8773&hourly=temperature_2m&forecast_days=1";
const string pcrurl="https://api.open-meteo.com/v1/forecast?latitude=41.6561&longitude=-0.8773&hourly=temperature_2m&forecast_days=1";
// las dos ultimas son tdl porque no he buscado coordenadas, busacrlas

#ifdef TEST

// Arrays to store 10 temperatures per city
float zaragozaTemps[10] = {22.0, 23.0, 21.5, 24.0, 25.0, 22.5, 23.5, 21.0, 22.0, 24.5};
float tudelaTemps[10] = {24.0, 25.5, 23.5, 24.5, 26.0, 24.0, 23.0, 25.0, 24.5, 23.0};
float gallocantaTemps[10] = {15.0, 16.5, 14.0, 15.5, 17.0, 16.0, 14.5, 15.0, 16.5, 14.0};
float pancrudoTemps[10] = {17.0, 18.0, 16.5, 17.5, 18.5, 17.0, 16.0, 17.0, 18.0, 16.5};

// Counters to keep track of which temperature to use
int zaragozaIndex = 0;
int tudelaIndex = 0;
int gallocantaIndex = 0;
int pancrudoIndex = 0;

// Function to simulate alternating test data for cities
void generateTestData() {
    // Zaragoza data
    ofstream zaragozaFile("files/zaragoza.txt");
    zaragozaFile << "Temperature: " << zaragozaTemps[zaragozaIndex] << " °C" << endl;
    zaragozaIndex = (zaragozaIndex + 1) % 10;  // Cycle through the array (0 to 9)

    // Tudela data
    ofstream tudelaFile("files/tudela.txt");
    tudelaFile << "Temperature: " << tudelaTemps[tudelaIndex] << " °C" << endl;
    tudelaIndex = (tudelaIndex + 1) % 10;  // Cycle through the array (0 to 9)

    // Gallocanta data
    ofstream gallocantaFile("files/gallocanta.txt");
    gallocantaFile << "Temperature: " << gallocantaTemps[gallocantaIndex] << " °C" << endl;
    gallocantaIndex = (gallocantaIndex + 1) % 10;  // Cycle through the array (0 to 9)

    // Pancrudo data
    ofstream pancrudoFile("files/pancrudo.txt");
    pancrudoFile << "Temperature: " << pancrudoTemps[pancrudoIndex] << " °C" << endl;
    pancrudoIndex = (pancrudoIndex + 1) % 10;  // Cycle through the array (0 to 9)
    
    cout << "Test data generated for all cities." << endl;
}
#endif





int main() {

    // Crea los sujetos
    ConcreteSubject* zaragoza = new ConcreteSubject("zaragoza.txt", zgzurl);
    ConcreteSubject* tudela = new ConcreteSubject("tudela.txt", tdlurl);
    ConcreteSubject* gallocanta = new ConcreteSubject("gallocanta.txt", gtcurl);
    ConcreteSubject* pancrudo = new ConcreteSubject("pancrudo.txt", pcrurl);
    
    // Crea observadores con un sujeto inicial (observer no tiene sentido sin al menos 1 obj)
    ConcreteObserver* observer_zaragoza = new ConcreteObserver(zaragoza, "Diego_mateo_zaragoza");
    ConcreteObserver* observer_ambiguo = new ConcreteObserver(zaragoza, "Carlos_solana_ambiguo");
    ConcreteObserver* observer_tudela = new ConcreteObserver(tudela, "Yago_torres_tudela");
    ConcreteObserver* observer_gallocanta = new ConcreteObserver(gallocanta, "Jorge_gallardo_gallocanta");
    ConcreteObserver* observer_pancrudo = new ConcreteObserver(pancrudo, "Martin_garcia_pancrudo");
    ConcreteObserver* observer_indeciso = new ConcreteObserver(pancrudo, "Unai_arronategui_indeciso");

    // Añadimos un par de sujetos más a los observadores 
    observer_ambiguo->AddSubject(tudela);
    observer_ambiguo->AddSubject(gallocanta);
    observer_ambiguo->AddSubject(pancrudo);

    observer_indeciso->AddSubject(zaragoza);


    // Ahora diremos a los sujetos que observadores tienen. Con esto conseguimos
    // que sepan A QUIÉN notificar, y no notifiquen indiscriminadamente a todos
    zaragoza->Attach(observer_zaragoza);
    tudela->Attach(observer_tudela);
    gallocanta->Attach(observer_gallocanta);
    pancrudo->Attach(observer_pancrudo);

    zaragoza->Attach(observer_ambiguo);
    tudela->Attach(observer_ambiguo);
    gallocanta->Attach(observer_ambiguo);
    pancrudo->Attach(observer_ambiguo);

    zaragoza->Attach(observer_indeciso);


    while (true) {

        #ifdef TEST
           generateTestData();
        #endif
        // Actualizar los datos de los observadores, notifica si hay nuevos a todos los observadores que tienen en su lista
        zaragoza->checkSources();
        tudela->checkSources();
        gallocanta->checkSources();
        pancrudo->checkSources();
        // Esperar 10 segundos aproximadamente antes de la siguiente actualización
        sleep(4);
        cout << "------------------------------------------------------------" << endl;
    }
}