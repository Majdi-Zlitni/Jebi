#!/usr/bin/env sh

##############################################################################
## Gradle start up script for POSIX generated manually
##############################################################################

APP_HOME="`dirname "$0"`"
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Determine Java
if [ -n "$JAVA_HOME" ] ; then
  JAVA_EXE="$JAVA_HOME/bin/java"
  if [ ! -x "$JAVA_EXE" ] ; then
    echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME" >&2
    exit 1
  fi
else
  JAVA_EXE="java"
fi

exec "$JAVA_EXE" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"