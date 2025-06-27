pipeline {
    agent any

    options {
        ansiColor('xterm')
    }

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

        stage('Capture Logs') {
            steps {
                sh '''
                    mkdir -p logs
                    docker logs carrental_carservice_1 > logs/carservice.log
                    docker logs carrental_carlistenerservice_1 > logs/carlistenerservice.log
                    docker logs carrental_caradminmonitor_1 > logs/caradminmonitor.log
                '''
            }
        }

        stage('Archive Logs') {
            steps {
                archiveArtifacts artifacts: 'logs/*.log', allowEmptyArchive: true
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
