#!/bin/bash

echo "🚀 Starting Irrigation Microservices with Docker Compose..."

# Check if docker is running and user has permissions
if ! docker info > /dev/null 2>&1; then
  echo "❌ Error: Docker is not running or you don't have permissions."
  echo "👉 Try running this script with sudo: sudo ./run_docker.sh"
  exit 1
fi

echo "🔨 Building and starting services..."
if ! docker compose up --build -d; then
    echo "❌ Error: Docker Compose failed to start services."
    exit 1
fi

echo "✅ Services started in background!"
echo "------------------------------------------------"
echo "🔌 Gateway:     http://localhost:8080"
echo "🔍 Eureka:      http://localhost:8761"
echo "🐰 RabbitMQ:    http://localhost:15672 (guest/guest)"
echo "------------------------------------------------"
echo "To see logs, run: docker compose logs -f"
echo "To stop, run: docker compose down"
