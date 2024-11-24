pipeline {
    agent any
    
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/joselizagaravito/app-java-texto.git'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    def image = docker.build("joselizagaravito/app-java-texto:${env.BUILD_NUMBER}")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-credentials-id') {
                        def image = docker.image("joselizagaravito/app-java-texto:${env.BUILD_NUMBER}")
                        image.push()
                    }
                }
            }
        }
    }
}
