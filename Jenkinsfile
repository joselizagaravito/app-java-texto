pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = "joselizagaravito/app-java-texto" // Nombre de la imagen con tu usuario de Docker Hub
        DOCKER_TAG = "latest"                                  // Etiqueta de la imagen
        DOCKER_REGISTRY = "docker.io"                          // Registro para Docker Hub
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

        stage('Build Application') {
            steps {
                echo "Compilando la aplicación Java..."
                // Asumiendo que estás usando Maven
                sh 'mvn clean package'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo "Ejecutando pruebas unitarias..."
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Construyendo la imagen Docker..."
                sh """
                docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .
                """
            }
        }

        stage('Push Docker Image') {
            steps {
                echo "Subiendo la imagen Docker a Docker Hub..."
                withCredentials([usernamePassword(credentialsId: 'docker-credentials-id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh """
                    echo "${DOCKERHUB_PASSWORD}" | docker login -u "${DOCKERHUB_USERNAME}" --password-stdin ${DOCKER_REGISTRY}
                    docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                    docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Limpieza de recursos...'
            sh 'docker logout'
        }
    }
}
