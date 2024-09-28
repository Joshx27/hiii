!/bin/bash

# Compilation step: Compile parser.java and related classes
echo "Compiling lexpl.java and related classes..."
javac lexpl.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful. Running the program..."

    # Define the example input string
    example_input="if x then print y else print z"
    # Run the compiled Java program with the predefined example input
    java lexpl "$example_input"
else
    echo "Compilation failed. Please check your code for errors."
fi