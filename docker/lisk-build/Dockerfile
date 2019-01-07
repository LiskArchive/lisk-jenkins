FROM ubuntu:xenial

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get --assume-yes install --no-install-recommends \
      wget ca-certificates gnupg2 jq \
      automake git python \
      build-essential autoconf libtool zlib1g-dev \
      tcl8.5 moreutils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Same uid & gid as jenkins docker slave
RUN groupadd --gid 6557 lisk && \
    useradd lisk --create-home --home-dir /home/lisk --shell /bin/bash --uid 6557 --gid 6557

USER lisk
RUN gpg2 --list-keys

WORKDIR /home/lisk/
