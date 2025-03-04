#include <vector>
#include <string.h>
#include "AbstractObserver.hpp"
#include <algorithm>
#include <iostream>


#ifndef ABSTRACTSUBJECT_HPP
#define ABSTRACTSUBJECT_HPP

using namespace std;

class AbstractSubject {
    private:
        string api_url;
    protected:
        vector<AbstractObserver*> observers;
        string mytxt;


public:
    AbstractSubject(string mYtxt, string url);

    void Attach(AbstractObserver* o);

    void Detach(AbstractObserver* o);

    void Notify(ConcreteSubject* subj);

    string UpdateInfo();
};

#endif
