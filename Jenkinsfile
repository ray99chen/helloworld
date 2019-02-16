podTemplate(label: 'mypod', containers: [
    containerTemplate(name: 'git', image: 'alpine/git', ttyEnabled: true, command: 'cat'),
    containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'helm', image: 'ibmcom/k8s-helm:v2.6.0',ttyEnabled: true,command: 'cat')
  ],
  volumes: [
    hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
  ]
  ) {
    node('mypod') {
        def commitId
        def imageRepository = 'ray99chen/helloworld'
        /*
        stage ('Git') {
            //checkout scm
            sh 'whoami'
            sh 'hostname -i'
            sh 'git clone -b master https://github.com/ray99chen/helloworld.git'
            commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        }
        */
    
        stage('Clone repository') {
            container('git') {
                sh 'whoami'
                sh 'hostname -i'
                sh 'git clone https://github.com/ray99chen/helloworld.git'
                sh 'echo a.'
                sh 'pwd'
                sh 'ls -ltr'
                dir('helloworld/') {
                    commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh 'echo b.'
                    sh 'ls -ltr'
                }
                sh "echo commitI=${commitId}"
            }
        }
        
        stage('Maven Build') {
            container('maven') {
                dir('helloworld/') {
                    sh 'hostname'
                    sh 'hostname -i'
                    sh 'mvn clean install'
                }
            }
        }
        /*
        def repository
        stage ('Docker') {
            container ('docker') {
                def registryIp = sh(script: 'getent hosts registry.kube-system | awk \'{ print $1 ; exit }\'', returnStdout: true).trim()
                repository = "${registryIp}:80/hello"
                sh "docker build -t ${repository}:${commitId} ."
                sh "docker push ${repository}:${commitId}"
            }
        }   
        */
        /* setup the credential-id with a username of password of the registry */
        stage ('Docker') {
            container ('docker') {
                docker.withRegistry('', 'dockerHubCredential') {
                    // def customImage = docker.build("my-image:${env.BUILD_ID}")
                    dir('helloworld/') {
                        def customImage = docker.build("${imageRepository}:${commitId}")
                        /* Push the container to the custom Registry */
                        customImage.push()
                    }
                }  
            }
        }
    
        stage ('Deploy') {
            container ('helm') {
                dir('helloworld/') {
                    sh 'echo in helm container'
                    sh 'pwd'
                    sh 'ls -ltr'
                    sh "/helm init --client-only --skip-refresh"
                    sh "/helm upgrade --install --wait --set image.repository=${imageRepository} --set image.tag=${commitId} -f ./helm/values.yaml helloworld ./helm"
                }
            }
        }        
    
    }
}
