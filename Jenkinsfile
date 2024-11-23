pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Simula un paso que podría fallar
                sh 'exit 1' // Provoca un error para pruebas
            }
        }
    }
}
