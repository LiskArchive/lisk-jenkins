

def call(Map params) {
	def liskBranch = params.branch
	def buildNetwork = params.network
	def useTestConfig = params.useTestConfig || false

	def liskDir = "${WORKSPACE}/lisk-${liskBranch}-${Math.abs(new Random().nextInt() % 1000) + 1}"
	def liskVersion = ""
	def liskReleaseName = ""

	def liskBuildDir = ""
	def liskBuildBranch = ""
	def latestVersion = false

	dir(liskDir) {
		git url: "https://github.com/LiskHQ/lisk.git", branch: liskBranch

		if(fileExists("genesisBlock.json")) {
			if(useTestConfig) {
				sh """
				cp test/genesisBlock.json .
				cp test/config.json .
				"""
			}
			sh """
			jq -r '.version' config.json > ".lisk-version"
			"""
		} else {
			if(useTestConfig) {
				sh """
				cp test/data/genesis_block.json .
				cp test/data/config.json .
				"""
			}
			sh """
			jq -r '.version' package.json > ".lisk-version"
			"""
		}
		sh """
		npm install
		node ./node_modules/.bin/grunt release
		"""
	}
	script {
		liskVersion = readFile("${liskDir}/.lisk-version").trim()

		if(fileExists("${liskDir}/genesisBlock.json")) {
			liskBuildBranch = "development"
			liskReleaseName = "${liskVersion}.tar.gz"
			latestVersion = false
		} else {
			liskBuildBranch = "1.0.0"
			liskReleaseName = "lisk-${liskVersion}.tgz"
			latestVersion = true
		}
		liskBuildDir = "${WORKSPACE}/lisk-build-${liskBuildBranch}-${Math.abs(new Random().nextInt() % 1000) + 1}"
	}
	dir(liskBuildDir) {
		git url: "https://github.com/LiskHQ/lisk-build.git", branch: liskBuildBranch
		sh """
		cp "${liskDir}/release/${liskReleaseName}" "src/"
		bash build.sh -n "${buildNetwork}" -v "${liskVersion}"
		"""
	}

	return [version: liskVersion, latestVersion: latestVersion, network: buildNetwork, file: "${liskBuildDir}/release/lisk-${liskVersion}-Linux-x86_64.tar.gz", nonVersionedFile: "${liskBuildDir}/release/lisk-Linux-x86_64.tar.gz"]
}
