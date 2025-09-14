#!/bin/sh
# Apache Maven Wrapper startup script
# Based on Apache Maven Wrapper 3.3.1
set -e

# Find the project base directory
find_maven_basedir() {
    # Search for .mvn directory starting from current directory up to root
    local dir=$(pwd)
    while [ "$dir" != "/" ] ; do
        if [ -d "$dir/.mvn" ]; then
            echo "$dir"
            return
        fi
        dir=$(dirname "$dir")
    done
    echo "$(pwd)"
}

MAVEN_PROJECTBASEDIR=$(find_maven_basedir)
WRAPPER_DIR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
WRAPPER_PROPERTIES="$WRAPPER_DIR/maven-wrapper.properties"

# Download wrapper jar if missing
if [ ! -f "$WRAPPER_JAR" ]; then
  if command -v curl >/dev/null 2>&1; then
    CURL="curl -fsSL"
    WRAPPER_URL=$(grep -E "^wrapperUrl=" "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
    mkdir -p "$WRAPPER_DIR"
    echo "Downloading Maven Wrapper JAR from $WRAPPER_URL"
    $CURL "$WRAPPER_URL" -o "$WRAPPER_JAR"
  elif command -v wget >/dev/null 2>&1; then
    WRAPPER_URL=$(grep -E "^wrapperUrl=" "$WRAPPER_PROPERTIES" | cut -d'=' -f2-)
    mkdir -p "$WRAPPER_DIR"
    echo "Downloading Maven Wrapper JAR from $WRAPPER_URL"
    wget -q "$WRAPPER_URL" -O "$WRAPPER_JAR"
  else
    echo "Error: curl or wget required to download Maven Wrapper." >&2
    exit 1
  fi
fi

JAVA_EXE="java"

# Exec wrapper with the required system property
exec "$JAVA_EXE" -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" -cp "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
