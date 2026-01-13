#!/bin/bash

echo "🚀 Starting Irrigation Microservices on Kubernetes..."

# 1. Check if Minikube is running
if ! minikube status | grep -q "Running"; then
    echo "❌ Minikube is not running. Starting it now..."
    minikube start
fi

# 2. Deploy Manifests
echo "📦 Applying Kubernetes manifests..."
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/rabbitmq.yaml
kubectl apply -f k8s/discovery.yaml
kubectl apply -f k8s/config.yaml
kubectl apply -f k8s/backend.yaml

# 3. Wait for services to be ready
echo "⏳ Waiting for pods to be ready (this may take a minute)..."
kubectl wait --for=condition=ready pod -l app=discovery-service --timeout=60s
kubectl wait --for=condition=ready pod -l app=config-service --timeout=60s

echo "✅ All services are deployed!"

# 4. Open services

echo "🔍 Opening Eureka Dashboard..."
minikube service discovery-service &

echo "------------------------------------------------"
echo "Check status with: kubectl get pods"
echo "Stop with: kubectl delete -f k8s/"
echo "------------------------------------------------"
