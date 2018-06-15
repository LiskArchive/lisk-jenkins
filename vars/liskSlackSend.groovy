#!/usr/bin/groovy

def call(String color, String message) {
  /* Slack channel names are limited to 21 characters */
  CHANNEL_MAX_LEN = 21

  channel = "${env.JOB_NAME}".tokenize('/')[0]
  channel = channel.replace('lisk-', 'lisk-ci-')
  if ( channel.size() > CHANNEL_MAX_LEN ) {
      channel = channel.substring(0, CHANNEL_MAX_LEN)
  }

  echo "[slack_send] channel: ${channel} "
  slackSend color: "${color}", message: "${message}", channel: "${channel}"
}
