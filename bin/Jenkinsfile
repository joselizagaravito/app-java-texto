pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = "joselizagaravito/app-java-texto" // Nombre de la imagen con tu usuario de Docker Hub
        DOCKER_TAG = "latest"                                // Etiqueta de la imagen
        DOCKER_REGISTRY = "docker.io"                        // Registro para Docker Hub
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']], // Rama master
                    userRemoteConfigs: [[
                        url: 'https://github.com/joselizagaravito/app-java-texto.git'
                    ]]
                ])
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker Image..."
                sh """
                docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                echo "Pushing Docker Image to Docker Hub..."
                sh """
                docker login -u <your-dockerhub-username> -p <your-dockerhub-password> ${DOCKER_REGISTRY} || exit 1
                docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                """
            }
        }
    }

}
