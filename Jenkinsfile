pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']], // Cambia a '*/master' si la rama principal es master
                    userRemoteConfigs: [[url: 'https://github.com/joselizagaravito/app-java-texto.git']]
                ])
            }
        }
        stage('Build Docker Image') {
            steps {
                echo "Building Docker Image..."
            }
        }
    }
}
