#!/bin/bash
set -x
DEST='C:/Program Files/sonarqube-5.6/extensions/plugins'
SRC=target
PLUGIN='sonar-resharper-plugin-*.jar'
rm "$DEST"/$PLUGIN
cp $SRC/$PLUGIN "$DEST"