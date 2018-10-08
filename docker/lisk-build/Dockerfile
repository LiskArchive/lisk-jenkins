FROM ubuntu:trusty

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get --assume-yes install --no-install-recommends \
      wget ca-certificates git jq \
      automake python \
      build-essential autoconf libtool zlib1g-dev \
      valgrind tcl8.5 moreutils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

ENV NVM_DIR /usr/local/nvm
ENV NODE_VERSION 6.14.2
ENV NVM_VERSION 0.33.11
ENV NVM_INSTALL_CHECKSUM bb7e8f51023ab8b054c6578591fa0dc361ceeb08744e5cd1f0e551235c4912b2

RUN mkdir -p $NVM_DIR && \
    wget -O /tmp/install_nvm.sh https://raw.githubusercontent.com/creationix/nvm/v$NVM_VERSION/install.sh && \
    echo "${NVM_INSTALL_CHECKSUM} /tmp/install_nvm.sh" | sha256sum --check && \
    bash /tmp/install_nvm.sh && \
    bash -c "source $NVM_DIR/nvm.sh && nvm install $NODE_VERSION && \
        nvm alias default $NODE_VERSION && nvm use default"

# Same uid & gid as jenkins docker slave
RUN groupadd --gid 6557 lisk && \
    useradd lisk --create-home --home-dir /home/lisk --shell /bin/bash --uid 6557 --gid 6557

USER lisk

ENV NODE_PATH $NVM_DIR/v$NODE_VERSION/lib/node_modules
ENV PATH $NVM_DIR/versions/node/v$NODE_VERSION/bin:$PATH

WORKDIR /home/lisk/
