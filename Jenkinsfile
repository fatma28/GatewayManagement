pipeline {
    agent any
    
    stages {
        stage ('Initialize') {
            steps {
                bat '''
                    echo "Initializing the project ..."
                ''' 
            }
        }

         stage ('Build') {
            steps {
                bat 'mvn clean install' 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}