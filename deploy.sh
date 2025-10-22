#!/bin/bash
set -e  # Exit on error

APP_DIR=~/app
JAR_FILE=$(ls $APP_DIR/target/*.jar | head -n 1)  # pick the first jar

echo "Loading environment variables from .env..."
export $(grep -v '^#' $APP_DIR/.env | xargs)

echo "Stopping previous app if running..."
pkill -f "java -jar" || true

echo "Starting new app..."
nohup java -jar "$JAR_FILE" > $APP_DIR/app.log 2>&1 &
echo "App deployed successfully!"
