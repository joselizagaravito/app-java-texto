pipeline {
    agent any

    tools {
        maven 'Maven 3.6.3'
    }

    environment {
        SUBSCRIPTION_ID = 'c9f87aca-4d37-4036-9ca6-42356791a21c'
        TENANT_ID = '799bf33c-dbbf-40f4-842a-ebce893cc3af'
        DOCKER_REGISTRY_URL = 'selacrjenkins.azurecr.io'
        RESOURCE_GROUP = 'GrupoDevOps'
        AKS_CLUSTER_NAME = 'ClusterDevOps'
        IMAGE_NAME = "${DOCKER_REGISTRY_URL}/app-java-texto:${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'github-credentials-id', usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASSWORD')]) {
                    git branch: 'master', url: "https://${GIT_USER}:${GIT_PASSWORD}@github.com/joselizagaravito/app-java-texto.git"
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Push Image to ACR') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'acr-credentials-id', usernameVariable: 'ACR_USER', passwordVariable: 'ACR_PASSWORD')]) {
                    sh "echo ${ACR_PASSWORD} | docker login ${DOCKER_REGISTRY_URL} -u ${ACR_USER} --password-stdin"
                    sh "docker push ${IMAGE_NAME}"
                }
            }
        }

        stage('Deploy to AKS') {
            steps {
                // Asumiendo que Azure CLI y kubectl ya están instalados en el agente
                withCredentials([usernamePassword(credentialsId: 'azure-credentials-id', usernameVariable: 'AZURE_CLIENT_ID', passwordVariable: 'AZURE_CLIENT_SECRET')]) {
                    sh """
                    az login --service-principal -u ${AZURE_CLIENT_ID} -p ${AZURE_CLIENT_SECRET} --tenant ${TENANT_ID}
                    az account set --subscription ${SUBSCRIPTION_ID}
                    """

                    sh """
                    if ! az aks show --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} >/dev/null 2>&1; then
                        az aks create --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} --node-count 1 --generate-ssh-keys
                    fi
                    """

                    sh "az aks get-credentials --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME}"

                    sh "kubectl apply -f k8s-deployment.yaml"
                }
            }
        }
    }
}
