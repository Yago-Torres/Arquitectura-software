#include <string>
#include "AbstractSubject.hpp"

#ifndef CONCRETESUBJECT_HPP
#define CONCRETESUBJECT_HPP

using namespace std;

class ConcreteSubject : public AbstractSubject {
    private:
        string subjectState;

    public:
        string GetState();
        void SetState (string state);
        ConcreteSubject(string mYtxt, string url);
        void getSources(); 
        
};

#endif
