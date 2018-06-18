#!/usr/bin/groovy

def call(String reason) {
  build_info = getBuildInfo()
  liskSlackSend('danger', "Build ${build_info} failed (<${env.BUILD_URL}/console|console>, <${env.BUILD_URL}/changes|changes>)\nCause: ${reason}")
  currentBuild.result = 'FAILURE'
  pr_branch = ''
  if (env.CHANGE_BRANCH != null) {
    pr_branch = " (${env.CHANGE_BRANCH})"
  }
  email_subject = "Build #${BUILD_NUMBER} of ${JOB_NAME}${pr_branch} failed"
  email_body = "${email_subject} - Check console output at $BUILD_URL to view the results."
  emailext subject: email_subject, body: email_body, recipientProviders: [culprits()]
  error("${reason}")
}
