#ifndef OBSERVER_HPP
#define OBSERVER_HPP

using namespace std;

class ConcreteSubject;

class AbstractObserver {
public:
    AbstractObserver();      
    virtual void Update(ConcreteSubject* subject) = 0;   
};

#endif
