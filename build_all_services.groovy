#!groovy

baseDestination = "/app/hedgeit"
sericeAccount = "svchedgeit"
WORKSPACE = "app/jenkins2/.jenkins/workspace/HedgeIt/Trunk"

node {

    stage('auditservice') {
        build 'auditservice'
    }

    stage('auditUI') {
        build 'auditUI'
    }
}