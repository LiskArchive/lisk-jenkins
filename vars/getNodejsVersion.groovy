def call() {
	script {
		nodejsVersion = readFile("${WORKSPACE}/.nvmrc").trim()
	}
	return nodejsVersion
}
