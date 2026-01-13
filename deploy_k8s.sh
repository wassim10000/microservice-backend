#!/bin/bash

echo "☸️  Deploying to Kubernetes..."

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    echo "Error: kubectl is not installed"
    exit 1
fi

# Check if docker is running and user has permissions
if ! docker info > /dev/null 2>&1; then
  echo "❌ Error: Docker is not running or you don't have permissions."
  echo "👉 Try running this script with sudo: sudo ./deploy_k8s.sh"
  exit 1
fi

echo "📦 Building Docker images into Minikube..."
# Build images directly in Minikube's registry so they are available without a push
if ! minikube image build -t discovery-service:latest ./discovery-service; then echo "❌ Build failed"; exit 1; fi
if ! minikube image build -t config-service:latest ./config-service; then echo "❌ Build failed"; exit 1; fi
if ! minikube image build -t energy-service:latest ./energy-service; then echo "❌ Build failed"; exit 1; fi
if ! minikube image build -t water-service:latest ./water-service; then echo "❌ Build failed"; exit 1; fi
if ! minikube image build -t gateway-service:latest ./gateway-service; then echo "❌ Build failed"; exit 1; fi

echo "🚀 Applying K8s manifests..."
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/rabbitmq.yaml
kubectl apply -f k8s/discovery.yaml
kubectl apply -f k8s/config.yaml
kubectl apply -f k8s/backend.yaml

echo "✅ Deployment applied!"
echo "Check pods with: kubectl get pods"
