pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/joselizagaravito/app-java-texto.git'
            }
        }
        
        stage('Build Application') {
            steps {
                // Construir el JAR usando Maven
                sh 'mvn clean package'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    // Construir la imagen de Docker, asegurando que el JAR esté en target/
                    def image = docker.build("joselizagaravito/app-java-texto:${env.BUILD_NUMBER}")
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    // Iniciar sesión en Docker Hub y subir la imagen
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-credentials-id') {
                        def image = docker.image("joselizagaravito/app-java-texto:${env.BUILD_NUMBER}")
                        image.push()
                    }
                }
            }
        }
    }
}
