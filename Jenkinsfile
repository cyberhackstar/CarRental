pipeline {
    agent any

    environment {
        COMPOSE_PROJECT_NAME = "carrental"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git 'https://github.com/cyberhackstar/CarRental.git'
            }
        }

        stage('Load .env') {
            steps {
                script {
                    def envFile = readFile('.env')
                    envFile.split('\n').each {
                        if (it && it.contains('=')) {
                            def (key, value) = it.tokenize('=')
                            env[key.trim()] = value.trim()
                        }
                    }
                }
            }
        }

        stage('Build and Deploy with Docker Compose') {
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
