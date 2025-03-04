#include "AbstractSubject.hpp"
#include "utils.hpp"
#include "json.hpp"
#include <fstream>

using json = nlohmann::json;
using namespace std;

AbstractSubject::AbstractSubject(string txt, string url) {
    api_url = url;
    mytxt = txt;
}

void AbstractSubject::Attach(AbstractObserver* o) {
    observers.push_back(o);
}

void AbstractSubject::Detach(AbstractObserver* o) {
    observers.erase(remove(observers.begin(), observers.end(), o), observers.end());  
}

void AbstractSubject::Notify(ConcreteSubject* subj) {
    for (auto o : observers) {
        o->Update(subj);
    }
}

string AbstractSubject::UpdateInfo() {
    string last_tmp;

    #ifdef TEST
        ifstream ifs("files/" + mytxt);
        if (!ifs.is_open()) {
            cout << "No se pudo abrir el archivo para lectura." << endl;
            return "";
        }

        string last_line;
        while (getline(ifs, last_line)) {
            last_tmp = last_line;  // Keep the last line (latest temperature)
        }
        ifs.close();

        if (last_tmp.empty()) {
            cout << "No data found in file. Returning empty." << endl;
            return "";
        }

    #else
        last_tmp = curl_manager(api_url);
        cout << "Normal Mode: API response: " << last_tmp << endl;

        // Append the new data to the file
        ofstream outfile("files/" + mytxt, ios::app);
        if (!outfile.is_open()) {
            cerr << "Error al abrir el archivo para escritura: files/" << mytxt << endl;
            return "";
        }

        outfile << last_tmp << endl;
        outfile.close();

        cout << "Normal Mode: Data appended to file: " << last_tmp << endl;
    #endif
    return last_tmp;
}