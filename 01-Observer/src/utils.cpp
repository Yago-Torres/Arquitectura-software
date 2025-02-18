
#include "utils.hpp"
#include <iostream>
#include <curl/curl.h>
#include "json.hpp"
#include <iostream>
#include <string>

using namespace std;
using json = nlohmann::json;


size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* output) {
    size_t totalSize = size * nmemb;
    output->append((char*)contents, totalSize); // Append the response data to the string
    return totalSize;
}

std::string treat_json(std::string& jsonString) {
    
    json parsedData = json::parse(jsonString);
    // ahora parsedData es igual que el json que retrieveo de la api. Se pueden retrievear más datos btw

    auto times = parsedData["hourly"]["time"];
    auto temperatures = parsedData["hourly"]["temperature_2m"];

    // para depuración, lista entera q recibo
    for (size_t i = 0; i < times.size(); ++i) {
        //cout << times[i] << ": " << temperatures[i] << " °C" << endl;
    }

    // Para depuración
    //cout << times.size() << endl;
    //cout << "The temperature at " << times[0] << " is " << temperatures[0] << " °C" << endl;
    
    return to_string(times[0]) + ";" + to_string(temperatures[0]);
}

std::string curl_manager(const std::string& url) {
    CURL* curl;
    CURLcode res;
    string response;

    curl = curl_easy_init();
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback); // Set the callback
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);         // Pass the string to store the response

        // Enable verbose for debugging (you can disable this later)
        // curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L);

        res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            cerr << "cURL Error: " << curl_easy_strerror(res) << endl;
        } else {
            response = treat_json(response);
        }

        curl_easy_cleanup(curl);
    } else {
        cerr << "Failed to initialize cURL" << endl;
    }

    return response; // Return the API response
}
