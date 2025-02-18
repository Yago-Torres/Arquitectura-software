#include "ConcreteObserver.hpp"

using namespace std;
    
ConcreteObserver::ConcreteObserver(ConcreteSubject* _subject){
    sujeto = _subject;
}

void ConcreteObserver::Update() {
    observerState = sujeto->GetState();
    cout << "Observer State: " << observerState << endl;
}
