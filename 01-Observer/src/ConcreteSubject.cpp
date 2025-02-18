#include "ConcreteSubject.hpp"
#include <iostream>
#include <fstream>
#include <string>

using namespace std;

ConcreteSubject::ConcreteSubject(string mYtxt, string url) : AbstractSubject(mYtxt, url) {
}

string ConcreteSubject::GetState() {
    return subjectState;
}

void ConcreteSubject::SetState (string state) {
    subjectState = state;
    cout << "Subject: My state has just changed to: " << subjectState << endl;
    this->Notify();
}


void ConcreteSubject::getSources() {
    this->checkSources();
    // mal hecho, en el checksources es dnd estoy cambiando el estado y dicendo a los observers que miren, por lo q siempre irán un cambio por detras. 
    // son las 1 am y me niego a tocar un alinea más de cmasmas hoy

    std::ifstream infile("files/" + mytxt);
    if (!infile.is_open()) {
        std::cerr << "Error al abrir el archivo: files/" << mytxt << std::endl;
        return;
    }
    // Leer la última línea del archivo
    std::string last_line, line;
    while (std::getline(infile, line)) {
        last_line = line;
    }
    infile.close();


    this->SetState(last_line);
}
