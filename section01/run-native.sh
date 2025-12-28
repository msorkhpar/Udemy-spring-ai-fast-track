#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

# Run the native image
./build/native/nativeCompile/spring-ai-claude "$@"
