pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = "joselizagaravito/app-java-texto"
        DOCKER_TAG = "latest"
        DOCKER_REGISTRY = "docker.io"
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']], // Cambia 'master' por el nombre de tu rama principal si es diferente
                    userRemoteConfigs: [[
                        url: 'https://github.com/joselizagaravito/app-java-texto.git'
                    ]]
                ])
            }
        }

        stage('Build Application') {
            steps {
                echo "Compilando la aplicación con Maven..."
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
                withCredentials([usernamePassword(credentialsId: 'docker-credentials-id', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh """
                    echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin
                    docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                    docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            echo "Limpieza del workspace y cierre de sesión de Docker..."
            sh 'docker logout'
        }
    }
}
