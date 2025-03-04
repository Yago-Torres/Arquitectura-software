#include <string>
#include <vector>
#include "AbstractObserver.hpp"
#include "ConcreteSubject.hpp"

#ifndef CONCRETEOBSERVER_HPP
#define CONCRETEOBSERVER_HPP

using namespace std;

class ConcreteObserver : public AbstractObserver {
    private:
        string observerState;
        vector<ConcreteSubject*> sujeto;
        // variable nombre para facilitar la visualización del funcionamiento
        string nombre;

        // método privado para buscar el índice del sujeto
        int buscarSujeto(ConcreteSubject* subject); 


    public:
        void Update(ConcreteSubject* subject) override;
        ConcreteObserver(ConcreteSubject* subject, string nombre);
        void AddSubject(ConcreteSubject* _subject);
};

#endif

