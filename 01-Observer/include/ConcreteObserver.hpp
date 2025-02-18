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

    public:
        void Update();
        ConcreteObserver(ConcreteSubject* subject);
};

#endif

