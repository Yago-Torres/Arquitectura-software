#ifndef UTILS_H
#define UTILS_H

#include <string>

size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* output);

std::string treat_json(const std::string& jsonString);

std::string curl_manager(const std::string& url);

#endif // UTILS_H
