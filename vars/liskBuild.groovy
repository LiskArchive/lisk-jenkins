

def call(Map params) {
	def liskBranch = params.branch
	def buildNetwork = params.network

	def liskDir = "/tmp/lisk-${liskBranch}-${Math.abs(new Random().nextInt() % 1000) + 1}"
	def liskVersion = ""
	def liskReleaseName = ""

	def liskBuildDir = ""
	def liskBuildBranch = ""
	def	latestVersion = false 

	dir(liskDir) {
		git url: "https://github.com/LiskHQ/lisk.git", branch: liskBranch

		sh """
		npm install
		node ./node_modules/.bin/grunt release
		jq -r '.version' config.json > ".lisk-version"
		"""
	}
	script {
		liskVersion = readFile("${liskDir}/.lisk-version").trim()

		if(fileExists("${liskDir}/genesisBlock.json")) {
			liskBuildBranch = "0.9.15"
			liskReleaseName = "${liskVersion}.tar.gz"
			latestVersion = false
		} else {
			liskBuildBranch = "1.0.0"
			liskReleaseName = "lisk-${liskVersion}.tgz"
			latestVersion = true
		}
		liskBuildDir = "/tmp/lisk-build-${liskBuildBranch}-${Math.abs(new Random().nextInt() % 1000) + 1}"
	}
	dir(liskBuildDir) {
		git url: "https://github.com/LiskHQ/lisk-build.git", branch: liskBuildBranch
		sh """
		cp "${liskDir}/release/${liskReleaseName}" "src/"
		bash build.sh -n "${buildNetwork}" -v "${liskVersion}"
		"""
	}

	return [version: liskVersion, latestVersion: latestVersion, network: buildNetwork, file: "${liskBuildDir}/release/lisk-${liskVersion}-Linux-x86_64.tar.gz"]
}
