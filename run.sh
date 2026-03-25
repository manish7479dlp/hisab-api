#!/bin/bash

APP_NAME=fms-app
IMAGE_NAME=fms-image

echo "🔨 Building Docker image..."
docker build -t $IMAGE_NAME .

echo "🛑 Stopping old container..."
docker stop $APP_NAME 2>/dev/null
docker rm $APP_NAME 2>/dev/null

echo "🚀 Starting container..."
docker run -it \
  --name $APP_NAME \
  -p 8080:8080 \
  $IMAGE_NAME

echo "✅ App running at http://localhost:8080"
