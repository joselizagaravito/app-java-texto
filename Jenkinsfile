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
    post {
        failure {
            emailext subject: "Pipeline Failed: ${env.JOB_NAME}",
                     body: """<p>El pipeline <b>${env.JOB_NAME}</b> falló en la build #${env.BUILD_NUMBER}.</p>
                              <p>Revisa los logs en <a href="${env.BUILD_URL}">Jenkins</a>.</p>""",
                     to: "inbox@your-mailtrap.com"
        }
    }
}

