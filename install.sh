#!/bin/bash
set -x
DEST='C:/Program Files/sonarqube-5.1.2/extensions/plugins'
SRC=target
PLUGIN='sonar-resharper-plugin*.jar'
rm "$DEST"/$PLUGIN
cp $SRC/$PLUGIN "$DEST"