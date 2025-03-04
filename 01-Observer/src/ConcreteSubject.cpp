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
    cout << endl << "Subject " << mytxt << ": My state has just changed to: " << subjectState << endl << endl;
    this->Notify(this);
}


void ConcreteSubject::checkSources() {
    SetState(AbstractSubject::UpdateInfo());
}
