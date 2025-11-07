#!/usr/bin/env bash
set -euo pipefail


NAMESPACE="fanworkersink"

echo "[INFO] Configurando entorno Docker de Minikube..."
eval $(minikube docker-env)

echo "[INFO] Construyendo imágenes Docker..."
docker build -t ms-fan-dispatcher ms-fan-dispatcher/.
docker build -t ms-sink-registry ms-sink-registry/.
docker build -t ms-worker-processor ms-worker-processor/.

echo "[INFO] Aplicando manifests en namespace $NAMESPACE..."
kubectl apply -k localstack/
kubectl apply -f ms-fan-dispatcher/deployment/k8s/ --recursive -n "$NAMESPACE"
kubectl apply -f ms-sink-registry/deployment/k8s/ --recursive -n "$NAMESPACE"
kubectl apply -f ms-worker-processor/deployment/k8s/ --recursive -n "$NAMESPACE"


kubectl rollout restart deployment/localstack -n fanworkersink
kubectl rollout restart deployment/ms-fan-dispatcher-deployment -n fanworkersink
kubectl rollout restart deployment/ms-worker-processor-deployment -n fanworkersink
kubectl rollout restart deployment/sink-registry-deployment -n fanworkersink




minikube service ms-fan-dispatcher-service -n fanworkersink