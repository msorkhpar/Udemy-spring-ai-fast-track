#!/bin/sh

# Check if model parameter is provided
if [ -z "$1" ]; then
    echo "Error: No model specified. Usage: $0 <model-name>"
    exit 1
fi

MODEL="$1"

# Start Ollama in the background
/bin/ollama serve &
pid=$!

# Wait for Ollama to be ready
echo "Waiting for Ollama to start..."
sleep 5

# Pull the model
echo "Pulling $MODEL model..."
ollama pull "$MODEL"

if [ $? -ne 0 ]; then
    echo "Error: Failed to pull model $MODEL"
    exit 1
fi

echo "Model $MODEL ready! Ollama is now serving on port 11434"

# Keep the container running
wait $pid