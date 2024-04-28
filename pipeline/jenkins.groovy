pipeline {
    agent any

    environment {
        REPO = 'https://github.com/lytvynov-kostiantyn/telegram_bot_on_go'
        BRANCH = 'main'
    }

    stages {

        stage('clone') {
            steps {
                echo 'Clone Repository'
                git branch: "${BRANCH}", url: "${REPO}"
            }
        }

        stage('test') {
            steps {
                echo 'Testing started'
                sh "make test"
            }
        }

        stage('build') {
            steps {
                echo "Building binary started"
                sh "make build"
            }
        }

        stage('image') {
            steps {
                echo "Building image started"
                sh "make image"
            }
        }

        stage('login to GHCR') {
            steps {
                sh "echo $GITHUB_TOKEN_PSW | docker login ghcr.io -u $GITHUB_TOKEN_USR --password-stdin"
            }
        }

        stage('push image') {
            steps {
              sh "make push"
            }
        }
    }
}