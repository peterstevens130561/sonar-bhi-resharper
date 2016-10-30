#!/bin/bash
set -x
DEST='C:/Program Files/sonarqube-5.6/extensions/plugins'
SRC=target
PLUGIN='sonar-resharper-plugin-5.1-SNAPSHOT.jar'
rm "$DEST"/$PLUGIN
cp $SRC/$PLUGIN "$DEST"