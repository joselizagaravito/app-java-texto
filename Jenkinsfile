pipeline {
    agent any

    tools {
        // Aseguramos la instalación de Maven automáticamente si no está configurado en el agente
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
        stage('Prepare Environment') {
            steps {
                // Instalación dinámica de herramientas necesarias en el agente
                sh '''
                apt-get update
                apt-get install -y git docker.io curl
                '''
            }
        }

        stage('Checkout Code') {
            steps {
                    git branch: 'master', url: "https://github.com/joselizagaravito/app-java-texto.git
            }
        }

        stage('Build') {
            steps {
                // Compilamos la aplicación con Maven
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Construimos la imagen Docker con la aplicación
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Push Image to ACR') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'acr-credentials-id', usernameVariable: 'ACR_USER', passwordVariable: 'ACR_PASSWORD')]) {
                    // Iniciamos sesión en Azure Container Registry
                    sh "echo ${ACR_PASSWORD} | docker login ${DOCKER_REGISTRY_URL} -u ${ACR_USER} --password-stdin"
                    // Subimos la imagen al ACR
                    sh "docker push ${IMAGE_NAME}"
                }
            }
        }

        stage('Deploy to AKS') {
            steps {
                // Iniciar sesión en Azure e interactuar con AKS
                withCredentials([usernamePassword(credentialsId: 'azure-credentials-id', usernameVariable: 'AZURE_CLIENT_ID', passwordVariable: 'AZURE_CLIENT_SECRET')]) {
                    sh '''
                    # Instalamos Azure CLI si no está presente
                    if ! command -v az &> /dev/null; then
                        curl -sL https://aka.ms/InstallAzureCLIDeb | bash
                    fi

                    # Iniciamos sesión en Azure
                    az login --service-principal -u ${AZURE_CLIENT_ID} -p ${AZURE_CLIENT_SECRET} --tenant ${TENANT_ID}
                    az account set --subscription ${SUBSCRIPTION_ID}
                    '''

                    // Verificamos y creamos el clúster de AKS si no existe
                    sh """
                    if ! az aks show --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} >/dev/null 2>&1; then
                        az aks create --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME} --node-count 1 --generate-ssh-keys
                    fi
                    """

                    // Obtenemos credenciales de AKS y desplegamos la aplicación
                    sh '''
                    az aks get-credentials --resource-group ${RESOURCE_GROUP} --name ${AKS_CLUSTER_NAME}

                    # Instalamos kubectl si no está presente
                    if ! command -v kubectl &> /dev/null; then
                        az aks install-cli
                    fi

                    # Desplegamos el manifiesto de Kubernetes
                    kubectl apply -f k8s-deployment.yaml
                    '''
                }
            }
        }
    }

    post {
        always {
            // Limpieza del workspace después de la ejecución
            cleanWs()
        }
        success {
            echo 'Pipeline ejecutado correctamente.'
        }
        failure {
            echo 'El pipeline falló. Revisa los logs para más detalles.'
        }
    }
}
