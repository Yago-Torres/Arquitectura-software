#ifndef OBSERVER_HPP
#define OBSERVER_HPP

using namespace std;

class AbstractObserver {
public:
    AbstractObserver();      
    virtual void Update() = 0;   
};

#endif
