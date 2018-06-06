#!/usr/bin/groovy

def call(String cache_file, String directory, Integer clean_old = 0, String exclude="") {
    def jobName = env.JOB_NAME.split('/')[0]
    try {
        if (exclude.length() > 0) {
            sh """
            if [ -f ${cache_file}.exclude ]; then
                if [ "\$(cat ${cache_file}.exclude )" != "${exclude}" ]; then
                    rm -v ${cache_file}
                fi
            fi 
            if [ ! -f "${cache_file}" ]; then
                GZIP=-4 tar czf "${cache_file}" ${directory} --exclude="${exclude}"
            fi
            echo ${exclude} > ${cache_file}.exclude
            """
        } 
        else {
            sh """
            if [ ! -f "${cache_file}" ]; then
                GZIP=-4 tar czf "${cache_file}" ${directory}
            fi
            """            
        }
        if (clean_old > 0){
            sh "find $HOME/cache/${jobName} -name '*.tar.gz' -ctime +${clean_old} -delete"
        }
    } catch (err) {
        echo "Error: ${err}"
    }
}
