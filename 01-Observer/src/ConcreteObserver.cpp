#include "ConcreteObserver.hpp"

using namespace std;
    
ConcreteObserver::ConcreteObserver(ConcreteSubject* _subject){
    sujeto.push_back(_subject);
}

void ConcreteObserver::Update() { //modificar para saber sobre que sujeto actualizas
    observerState = sujeto->GetState(); // vector de states en el observer supongo
    cout << "Observer State: " << observerState << endl;
}
