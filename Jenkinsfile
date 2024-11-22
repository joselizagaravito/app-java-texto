// Jenkinsfile

// Declaramos el pipeline en modo declarativo
pipeline {
    agent any

    // Definimos las herramientas necesarias
    tools {
        // Asumiendo que necesitamos Maven para compilar una aplicación Java
        maven 'Maven 3.6.3'
    }

    // Definimos las variables de entorno necesarias
    environment {
        // Obtenemos las credenciales desde el almacén de Jenkins
        GIT_CREDENTIALS = credentials('github-credentials-id')
        AZURE_CREDENTIALS = credentials('azure-credentials-id')
        SUBSCRIPTION_ID = 'c9f87aca-4d37-4036-9ca6-42356791a21c'
        TENANT_ID = '799bf33c-dbbf-40f4-842a-ebce893cc3af'
        DOCKER_REGISTRY_CREDENTIALS = credentials('acr-credentials-id')
        DOCKER_REGISTRY_URL = 'selacrjenkins.azurecr.io'
        RESOURCE_GROUP = 'GrupoDevOps'
        AKS_CLUSTER_NAME = 'ClusterDevOps'
        IMAGE_NAME = "${DOCKER_REGISTRY_URL}/app-java-texto:${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Clonamos el repositorio usando las credenciales almacenadas en Jenkins
                git branch: 'main', credentialsId: 'github-credentials-id', url: 'https://github.com/joselizagaravito/app-java-texto.git'
            }
        }

        stage('Build') {
            steps {
                // Compilamos el código fuente usando Maven
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Construimos la imagen de Docker a partir del código compilado
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Push Image to ACR') {
            steps {
                // Iniciamos sesión en Azure Container Registry (ACR)
                sh "echo ${DOCKER_REGISTRY_CREDENTIALS_PSW} | docker login ${DOCKER_REGISTRY_URL} -u ${DOCKER_REGISTRY_CREDENTIALS_USR} --password-stdin"
                // Subimos la imagen al ACR
                sh "docker push ${IMAGE_NAME}"
            }
        }

        stage('Deploy to AKS') {
            steps {

                // Instalamos Azure CLI si no está instalado
                sh 'curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash'

                // Iniciar sesión en Azure
                sh """
                az login --service-principal -u ${AZURE_CREDENTIALS_USR} -p ${AZURE_CREDENTIALS_PSW} --tenant ${TENANT_ID}
                az account set --subscription ${SUBSCRIPTION_ID}
                """

                // Verificar y crear el clúster AKS si no existe
                sh """
                if ! az aks show --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} >/dev/null 2>&1; then
                    az aks create --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} --node-count 1 --generate-ssh-keys
                fi
                """

                // Obtenemos las credenciales del clúster de AKS
                sh "az aks get-credentials --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME}"

                // Desplegamos el contenedor en AKS usando el manifiesto de Kubernetes
                sh "kubectl apply -f k8s-deployment.yaml"
            }
        }
    }
}
