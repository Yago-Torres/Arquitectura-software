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

void AbstractSubject::Notify() {
    for (auto o : observers) {
        o->Update();
    }
}

void AbstractSubject::checkSources() {

    string last_tmp = curl_manager(api_url);
    cout << last_tmp << endl;

    size_t separator_pos = last_tmp.find(';');
    std::string fecha = last_tmp.substr(0, separator_pos);

    ifstream ifs("files/" + mytxt);
    if (!ifs.is_open()) {
        cout << "No se pudo abrir el archivo." << endl;
        return;
    }

    string last_line, line;
    while (getline(ifs, line)) {
        last_line = line;
    }
    ifs.close();

    if (!last_line.empty()) {
        size_t last_separator_pos = last_line.find(';');
        if (last_separator_pos != string::npos) {
            string last_fecha = last_line.substr(0, last_separator_pos);
            if (last_fecha == fecha) {
                // La fecha coincide, no hacemos nada
                cout << "La fecha ya está registrada: " << fecha << endl;
                return;
            }
        }
    }

    // La fecha no coincide, agregamos la nueva línea
    std::ofstream outfile("files/" + mytxt, ios::app);
    if (!outfile.is_open()) {
        std::cerr << "Error al abrir el archivo para escritura: files/" << mytxt << std::endl;
        return;
    }
    outfile << last_tmp << std::endl;
    outfile.close();
    // Ejecutar la función notify
    Notify();
}