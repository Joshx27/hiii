#!/bin/bash

# Check if at least one argument (file or directory) is provided
if [ "$#" -lt 1 ]; then
    echo "Usage: $0 <file_or_directory> [<file_or_directory> ...]"
    exit 1
fi

# Full path to cpplint.py (update the path to where cpplint.py is located)
CPPLINT_PATH="cpplint/cpplint.py"

# Loop through all provided files and directories and run cpplint
for target in "$@"; do
    echo "Running cpplint on $target..."
    python3 "$CPPLINT_PATH" --recursive "$target" 2>&1 | tee lint_output.txt
done

echo "Linting completed. Output saved to lint_output.txt."
