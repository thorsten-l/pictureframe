#!/bin/sh

cd $HOME

wget -q -O - https://download.bell-sw.com/pki/GPG-KEY-bellsoft | apt-key add -
echo "deb [arch=`dpkg --print-architecture`] https://apt.bell-sw.com/ stable main" | tee /etc/apt/sources.list.d/bellsoft.list
apt update
apt install bellsoft-java11 unclutter unzip

unzip autostart.zip
