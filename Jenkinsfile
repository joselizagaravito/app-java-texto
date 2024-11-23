pipeline {
    agent any

    stages {
        stage('Verificar Codigo') {
            steps {
                git branch: 'master', url: 'https://github.com/joselizagaravito/app-java-texto.git'
            }
        }
        stage('Build') {
            steps {
                sh '${MAVEN_HOME}/bin/mvn clean package'
            }
        }
        stage('Testing'){
                sh '${MAVEN_HOME}/bin/mvn test'
        }
    }
}
