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

        stage('Deploy Monitoring Stack') {
            steps {
                dir('monitoring') {
                    sh '''
                        docker-compose down --remove-orphans || echo "Monitoring stack cleanup skipped"
                        docker-compose up -d || echo "Monitoring stack deployment failed"
                    '''
                }
            }
        }

        stage('Clean Previous Containers') {
            steps {
                sh '''
                    docker-compose down --remove-orphans --timeout 10 || echo "Cleanup warning: containers or network may still be active"
                '''
            }
        }

        stage('Build and Deploy Services') {
            steps {
                sh '''
                    docker-compose build --no-cache || echo "Build failed"
                    docker-compose up -d || echo "Deployment failed"
                '''
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
                sh '''
                    docker-compose logs carservice || echo "Console logs for carservice unavailable"
                    docker-compose logs carlistnerservice || echo "Console logs for carlistnerservice unavailable"
                    docker-compose logs caradminmonitor || echo "Console logs for caradminmonitor unavailable"
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

// This Jenkinsfile is designed to automate the deployment of a microservices architecture for a car rental application.
// It includes stages for injecting environment variables, deploying a monitoring stack, cleaning up previous containers,