pipeline {
    agent any
    environment{
        MAVEN_HOME = '/usr/share/maven' //ruta de maven
    }
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
            steps {
                sh '${MAVEN_HOME}/bin/mvn test'
            }
        }
        stage('Build Docker Image'){
            steps {
                script{
					docker.build("joselizagaravito/app-java-texto:{env.BUILD_NUMBER}")
				}
            }
        }
    }
}
