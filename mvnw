#!/bin/sh
# Apache Maven Wrapper startup script
# Based on Apache Maven Wrapper 3.3.1
set -e

WRAPPER_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P)/.mvn/wrapper"
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

# Exec wrapper
exec "$JAVA_EXE" -cp "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
