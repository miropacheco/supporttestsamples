@REM $Copyright(c) 2009 Progress Software Corporation (PSC). All rights reserved.$
@ECHO OFF

set JAVA_EXE=java

set ANT_HOME=%ANT_HOME%
set CLASSPATH=%ANT_HOME%/lib/ant-launcher.jar

"%JAVA_EXE%" -classpath "%CLASSPATH%" -Dant.home="%ANT_HOME%" org.apache.tools.ant.launch.Launcher -f build.xml %1 %2

