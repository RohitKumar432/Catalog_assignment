#include <iostream>
#include <fstream>
#include <json/json.h>  // Include jsoncpp library for JSON parsing
#include <vector>
#include <cmath>

// Function to convert a number from a given base to decimal
long long baseToDecimal(const std::string& value, int base) {
    long long decimalValue = 0;
    for (char digit : value) {
        decimalValue = decimalValue * base + (isdigit(digit) ? digit - '0' : digit - 'a' + 10);
    }
    return decimalValue;
}

// Structure to store each root (x, y)
struct Root {
    int x;
    long long y;
};

// Function to parse JSON input and get roots
std::vector<Root> parseJSON(const std::string& filename, int& k) {
    std::ifstream file(filename);
    Json::Value data;
    file >> data;
    
    std::vector<Root> roots;
    k = data["keys"]["k"].asInt();
    for (const auto& key : data.getMemberNames()) {
        if (key != "keys") {
            int x = std::stoi(key);
            int base = data[key]["base"].asInt();
            std::string value = data[key]["value"].asString();
            long long y = baseToDecimal(value, base);
            roots.push_back({x, y});
        }
    }
    return roots;
}

// Function to calculate the constant term (c) using Lagrange interpolation
long long findConstantTerm(const std::vector<Root>& roots, int k) {
    long long constantTerm = 0;
    
    for (int i = 0; i < k; i++) {
        long long term = roots[i].y;
        
        for (int j = 0; j < k; j++) {
            if (i != j) {
                term *= -roots[j].x;
                term /= (roots[i].x - roots[j].x);
            }
        }
        
        constantTerm += term;
    }
    
    return constantTerm;
}

int main() {
    int k;
    std::vector<Root> roots = parseJSON("input.json", k);
    long long constantTerm = findConstantTerm(roots, k);

    std::cout << "The constant term (secret) is: " << constantTerm << std::endl;
    return 0;
}