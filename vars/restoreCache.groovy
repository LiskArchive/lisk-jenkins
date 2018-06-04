#!/usr/bin/groovy

def call(String file) {
    try {
        sh """
        cache_dir="\$HOME/cache/${URLDecoder.decode(JOB_NAME)}"
        mkdir -p \$cache_dir
        cache_file=\"\$cache_dir/\$( sha1sum ${file} |awk '{ print \$1 }' ).tar.gz\"
        if [ -f "\$cache_file" ]; then
            tar xf "\$cache_file"
        fi
        echo \$cache_file > .cache_file
        """
    } catch (err) {
        echo "Error: ${err}"
    }
    return readFile(".cache_file").trim()
}
