#!/bin/sh

#################################################
#
#  Apama Ant Startup Script
# 
#
#################################################

JAVA_EXE=java

ANT_HOME=./lib/ant
CLASSPATH=$ANT_HOME/lib/ant-launcher.jar

"$JAVA_EXE" -classpath "$CLASSPATH" -Dant.home="$ANT_HOME%" org.apache.tools.ant.launch.Launcher -f build.xml $1 $2


exit 0
