pipeline {
    agent { label 'android' }

    environment {A
        APK_OUTPUT_PATH = "app/build/outputs/apk/release/app-release.apk"
//         APK_OUTPUT_PATH = "app/build/outputs/apk/release/app-release-unsigned.apk"
        DEST_DIR = "/var/www/selp-android"
    }

    stages {
        stage('Check workspace') {
            steps {
                sh 'pwd && ls -al'
            }
        }

        stage('Prepare Build Environment') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean'
            }
        }

        stage('Build APK') {
            steps {
                sh './gradlew assembleRelease'
            }
        }

        stage('Copy APK to Web Server') {
            steps {
                sh "mkdir -p ${DEST_DIR}"
                sh "cp ${APK_OUTPUT_PATH} ${DEST_DIR}/latest.apk"
            }
        }
    }

    post {
        failure {
            echo "❌ 빌드 실패! 콘솔 로그를 확인하세요."
        }
        success {
            echo "✅ APK가 성공적으로 빌드 및 배포되었습니다: ${DEST_DIR}/latest.apk"
        }
    }
}