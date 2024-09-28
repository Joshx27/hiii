#include <iostream>
#include <string>

class Simplelex
{
public:
    enum State
    {
        q1,
        q2
    };

    enum TokenType
    {
        binaryNumber,
        invalid
    };

    static TokenType classifyToken(std::string input)
    {

        State currentState = q1;

        for (char c : input)
        {

            if (c != '0' && c != '1')
                return invalid;

            if (currentState == q1)
            {

                if (c == '0')
                    currentState = q2;

                if (c == '1')
                    currentState = q1;
            }

            else if (currentState == q2)
            {

                if (c == '1')
                    currentState = q1;
                if (c == '0')
                    currentState = q2;
            }
        }

        if (currentState == q1)
            return invalid;

        return binaryNumber;
    }
};

int main()
{
    std::string input = "";

    std::cout << "Enter number: ";
    std::cin >> input;

    Simplelex::TokenType tokenType = Simplelex::classifyToken(input);

    if (tokenType == Simplelex::TokenType::binaryNumber)
    {
        std::cout << "The token is a binary number: " << input << "\n";
    }
    else if (tokenType == Simplelex::TokenType::invalid)
    {
        std::cout << "The token is an invalid binary number: " << input << "\n";
    }
}