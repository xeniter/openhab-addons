#!/bin/bash

export PATH=/usr/lib/jvm/java-17-openjdk-amd64/bin:/home/mdipolt/.pyenv/shims:/home/mdipolt/.pyenv/bin:/usr/local/bin:/usr/bin:/bin:/usr/local/games:/usr/games:/opt/texlive/bin/x86_64-linux:/opt/uClinux/bfin-elf/bin:/opt/swat-git-tools/bin:/opt/rtems/4.11/bin/:/opt/gdbproxy/bin/:/home/mdipolt/gits/poky/bitbake/bin/:/usr/local/go/bin:/home/mdipolt/work/bin
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64


/home/mdipolt/Downloads/openhab/runtime/bin/stop
rm -rf /home/mdipolt/Downloads/openhab/userdata/cache
rm -rf /home/mdipolt/Downloads/openhab/userdata/tmp
set -e
#mvn -o clean install -DskipChecks
mvn clean install -DskipChecks
#mvn clean install -DskipChecks

cp /home/mdipolt/gits/openhab-addons/bundles/org.openhab.binding.romyrobot/target/org.openhab.binding.romyrobot-4.1.0-SNAPSHOT.jar /home/mdipolt/Downloads/openhab/addons/
/home/mdipolt/Downloads/openhab/start.sh debug
