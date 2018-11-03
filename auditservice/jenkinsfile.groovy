#!grooy
node {

    // Mark the code checkout 'stage'....
    stage('Check Out') {

        // Checkout code from repository
        // NOte that env for release version does not get expanded properly so need to do it manually
        def scmVars = scm
        def url = scmVars.getLocations()[0].getURL()
        def newURL = url.replace('$(RELEASEVERSION)', env.RELEASEVERSION)


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

    try {
        stage ('Build') {
            gradle('clean build -Duser.timezone=GMT -Dspring.profiles.active=test')
        }
        
        stage ('Archive') {
            // Archive the artifact
            step([$class: 'ArtifactArchiver', artifacts: '**/build/libs/*.jar', fingerprint: true])
        }
    } finally {
        step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/test/TEST-*.xml'])
    }
}

def gradle(args) {
    if (isUnix()) {
        gradleScript = "${tool 'GRADLE'}/bin/gradle"
        sh "${gradleScript} ${args}"
    } else {
        bat "${gradleScript} ${args}"
    }
}