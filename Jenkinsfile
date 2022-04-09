#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }

    docker.image('jhipster/jhipster:v7.7.0').inside('-u jhipster -e MAVEN_OPTS="-Duser.home=./"') {
        stage('check java') {
            bat "java -version"
        }

        stage('clean') {
            bat "chmod +x mvnw"
            bat "./mvnw -ntp clean -P-webapp"
        }
        stage('nohttp') {
            bat "./mvnw -ntp checkstyle:check"
        }

        stage('install tools') {
            bat "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:install-node-and-npm@install-node-and-npm"
        }

        stage('npm install') {
            bat "./mvnw -ntp com.github.eirslett:frontend-maven-plugin:npm"
        }
        stage('backend tests') {
            try {
                bat "./mvnw -ntp verify -P-webapp"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/surefire-reports/TEST-*.xml,**/target/failsafe-reports/TEST-*.xml'
            }
        }

        stage('frontend tests') {
            try {
               bat "npm install"
               bat "npm test"
            } catch(err) {
                throw err
            } finally {
                junit '**/target/test-results/TESTS-results-jest.xml'
            }
        }

        stage('packaging') {
            bat "./mvnw -ntp verify -P-webapp -Pprod -DskipTests"
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }

    def dockerImage
    stage('publish docker') {
        // A pre-requisite to this step is to setup authentication to the docker registry
        // https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#authentication-methods
        bat "./mvnw -ntp -Pprod verify jib:build"
    }
}
