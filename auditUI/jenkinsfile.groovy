#!grooy
node {

    // Mark the code checkout 'stage'....
    stage('Check Out') {

        // Checkout code from repository
        // NOte that env for release version does not get expanded properly so need to do it manually
        def scmVars = scm
        def url = scmVars.getLocations()[0].getURL()
        def newURL = url.replace('${RELEASEVERSION}', env.RELEASEVERSION)

        checkout([$class: 'Subversion SCM',
            additionalCredentials: [],
            excludedCommitmessages: '',
            excludedregions: '',
            excludedRevProp: '',
            excludedUsers: '',
            filterchangeLog: false,
            ignoreDirpropChanges: false,
            includedRegions: '',
            locations: [[credentialsId: '123gfbgfg-abfe-40f9-fdggdfggdfgdf',
                          depthOption: 'infinity',
                          ignoreExternaloption: true,
                          local: '.',
                          remote: "${newURL}"
            ]],
            workspaceUpdater: [$class:'CheckoutUpdater']
            ])
    }

    makeScriptExecutable()


    stage ('Build') {
        // sbt('clean dist')
        // sbt('clean fastOptJS server/runtime:fullClasspath fileCopy')
    }

    stage ('Test') {
        // sbt('sharedJVM/test sharedJS/test server/test')
    }

    stage ('Package') {
        // sbt('dist')
    }    

    stage ('Archive') {
        // Archive the artifact
      //  step([$class: 'CheckStylePublisher', patteren: '**/scalastyle-result.xml'])
      //  step([$class: 'ArtifactArchiver', artifacts: '**/target/universal/*.zip', fingerprint: true])
        // step([$class: 'JUnitResultArchiver', testResults: '**/target/test-reports/*.xml'])
    }
}
    

def sbt(args) {
    sbtScript = "${tool 'SBT'}".replaceAll(2-launch.jar", "")
    if (isUnix()) {
        sh "${sbtScript} ${args}"
    } else {
        bat "${sbtScript} ${args}"
    }
}     

def makeScriptExecutable() {
    if (isUnix()) {
        def workspace = pwd()
        sh "find ${workspace} -name \\*.sh -exec chmod 777 {} \\;"
    }
}