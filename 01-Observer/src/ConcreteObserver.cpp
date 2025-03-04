#include "ConcreteObserver.hpp"

using namespace std;
    
ConcreteObserver::ConcreteObserver(ConcreteSubject* _subject, string nombre){
    sujeto.push_back(_subject);
    this->nombre = nombre;
}

void ConcreteObserver::AddSubject(ConcreteSubject* _subject){
    sujeto.push_back(_subject);
}

// función que usamos para buscar el índice del sujeto, y no tener que
// actualizar todos
int ConcreteObserver::buscarSujeto(ConcreteSubject* subject) {
    for (size_t i = 0; i < sujeto.size(); ++i) {
        if (sujeto[i] == subject) {
            return i;  // Devuelve el índice
        }
    }
    return -1;  // NO DEBERÍA PASAR, dado que para que esto se ejecute el observer me conoce
}

void ConcreteObserver::Update(ConcreteSubject* subject) { 
    observerState = sujeto[buscarSujeto(subject)]->GetState(); 
    cout << "Soy el observer " << nombre << " y mi estado es: " << observerState << endl;
}
