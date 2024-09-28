#!/bin/bash

# Check if a filename was provided as an argument
if [ $# -eq 0 ]; then
  echo "Usage: $0 <cpp_filename>"
  exit 1
fi

# Set the C++ file and output binary names
CPP_FILE=$1
OUTPUT_FILE=${CPP_FILE%.cpp}.out

# Compile the C++ file
g++ -o $OUTPUT_FILE $CPP_FILE

# Check if the compilation was successful
if [ $? -eq 0 ]; then
  echo "Compilation successful. Running the program..."

  # Test valid input (e.g., "010")
  echo "Testing valid case (input: '010'):"
  echo "010" | ./$OUTPUT_FILE

  # Test invalid input (e.g., "0111")
  echo "Testing invalid case (input: '0111'):"
  echo "0111" | ./$OUTPUT_FILE

else
  echo "Compilation failed."
fi
