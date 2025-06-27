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

        stage('Capture Full Logs') {
            steps {
                sh '''
                    mkdir -p logs
                    docker-compose logs carservice > logs/carservice.log
                    docker-compose logs carlistnerservice > logs/carlistenerservice.log
                    docker-compose logs caradminmonitor > logs/caradminmonitor.log
                '''
            }
        }

        stage('Capture Startup Logs Only') {
            steps {
                sh '''
                    mkdir -p logs
                    docker-compose logs carservice | grep -iE "Started|Tomcat started|Started Application" > logs/carservice_startup.log
                    docker-compose logs carlistnerservice | grep -iE "Started|Tomcat started|Started Application" > logs/carlistenerservice_startup.log
                    docker-compose logs caradminmonitor | grep -iE "Started|Tomcat started|Started Application" > logs/caradminmonitor_startup.log
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
