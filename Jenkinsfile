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
                    sh '''
                        echo "Current user: $(whoami)"
                        ls -l
                        cp -f $ENV_FILE .env
                    '''
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

        stage('Wait for Services to Start') {
            steps {
                sh 'sleep 10'
            }
        }

        stage('Copy Internal Log Files') {
            steps {
                sh '''
                    mkdir -p logs
                    docker cp carrental_carservice_1:/app/logs/CarService.log logs/carservice_internal.log || echo "carservice log not found"
                    docker cp carrental_carlistnerservice_1:/app/logs/CarService.log logs/carlistenerservice_internal.log || echo "carlistenerservice log not found"
                    docker cp carrental_caradminmonitor_1:/app/logs/CarService.log logs/caradminmonitor_internal.log || echo "caradminmonitor log not found"
                '''
            }
        }

        stage('Capture Startup Logs Only') {
            steps {
                sh '''
                    docker-compose logs carservice | grep -iE "Started|Tomcat started|Started Application" > logs/carservice_startup.log || echo "No startup logs for carservice"
                    docker-compose logs carlistnerservice | grep -iE "Started|Tomcat started|Started Application" > logs/carlistenerservice_startup.log || echo "No startup logs for carlistenerservice"
                    docker-compose logs caradminmonitor | grep -iE "Started|Tomcat started|Started Application" > logs/caradminmonitor_startup.log || echo "No startup logs for caradminmonitor"
                '''
            }
        }

        stage('Print Logs to Console') {
            steps {
                sh 'docker-compose logs carservice'
                sh 'docker-compose logs carlistnerservice'
                sh 'docker-compose logs caradminmonitor'
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
