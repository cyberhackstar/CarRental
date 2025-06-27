pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = "carrental"
    }

    stages {
        stage('Inject .env') {
            steps {
                withCredentials([file(credentialsId: 'carrental-env', variable: 'ENV_FILE')]) {
                    sh 'cp $ENV_FILE .env'
                }
            }
        }

        stage('Build and Deploy') {
            steps {
                sh 'docker-compose down'
                sh 'docker-compose build'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful!'
        }
        failure {
            echo '❌ Deployment failed.'
        }
    }
}
