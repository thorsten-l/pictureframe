#!/bin/sh

cd /home/pi
export DISPLAY=:0.0

echo turning screen blank off
sleep 2
xset s off
xset -dpms
xset s noblank

sleep 2
echo turning mouse cursor off
unclutter -idle 0 &

mv logs logs.`date '+%Y%m%d-%H%M%S'`

./pictureframe.jar
