 resource "kubectl_manifest" "is-my-burguer-namespace" {
  depends_on = [
    data.aws_eks_cluster.cluster
  ]
  yaml_body = <<YAML
apiVersion: apps/v1
kind: Namespace
apiVersion: v1
metadata:
  name: is-my-burguer
  namespace: is-my-burguer
  labels:
    name: is-my-burguer
    app: is-my-burguer
YAML
}

resource "kubectl_manifest" "is-my-burguer-sd-deployment" {
  depends_on = [
    data.aws_eks_cluster.cluster,
    kubectl_manifest.is-my-burguer-namespace
  ]
  yaml_body = <<YAML
apiVersion: apps/v1
kind: Deployment
metadata:
  name: is-my-burguer-sd
  namespace: is-my-burguer
  labels:
    name: is-my-burguer-sd
    app: is-my-burguer-sd
spec:
  replicas: 1
  selector:
    matchLabels:
      app: is-my-burguer-sd
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: is-my-burguer-sd
    spec:
      containers:
        - name: is-my-burguer-sd
          resources:
            limits:
              cpu: "1"
              memory: "300Mi"
            requests:
              cpu: "300m"
              memory: "300Mi"
          env:
          image: docker.io/ismaelgcosta/is-my-burguer-sd:is-my-burguer-sd-1.1.1
          ports:
            - containerPort: 443
      restartPolicy: Always
status: {}
YAML
}


resource "kubectl_manifest" "is-my-burguer-sd-lb" {
  depends_on = [
    data.aws_eks_cluster.cluster,
    kubectl_manifest.is-my-burguer-sd-deployment,
    kubectl_manifest.is-my-burguer-namespace
  ]
  yaml_body = <<YAML
apiVersion: v1
kind: Service
metadata:
  name: is-my-burguer-sd-lb
  namespace: is-my-burguer
spec:
  type: LoadBalancer
  selector:
    app: is-my-burguer-sd
  ports:
    - name: https
      protocol: TCP
      port: 443
      targetPort: 443
      nodePort: 31443
YAML
}

resource "kubectl_manifest" "is-my-burguer-sd-hpa" {
  depends_on = [
    data.aws_eks_cluster.cluster,
    kubectl_manifest.is-my-burguer-sd-deployment,
    kubectl_manifest.is-my-burguer-sd-lb,
    kubectl_manifest.is-my-burguer-namespace
  ]
  yaml_body = <<YAML
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: is-my-burguer-sd-hpa
  namespace: is-my-burguer
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: is-my-burguer-sd
    namespace: is-my-burguer
  minReplicas: 2
  maxReplicas: 2
  behavior:
    scaleDown:
      stabilizationWindowSeconds: 0 # para forçar o kubernets a zerar a janela de tempo e escalar imediatamente
    scaleUp:
      stabilizationWindowSeconds: 0 # para forçar o kubernets a zerar a janela de tempo e escalar imediatamente
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 1 # para forçar o kubernets escalar com 1% de cpu
status:
  observedGeneration: 0
  lastScaleTime:
  currentReplicas: 0
  desiredReplicas: 2
  currentMetrics:
  - type: Resource
    resource:
      name: cpu
YAML
}



