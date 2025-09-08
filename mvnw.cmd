@ECHO OFF
REM Apache Maven Wrapper startup script for Windows
REM Based on Apache Maven Wrapper 3.3.1

SETLOCAL
SET WRAPPER_DIR=%~dp0.mvn\wrapper
SET WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
SET WRAPPER_PROPERTIES=%WRAPPER_DIR%\maven-wrapper.properties

IF NOT EXIST "%WRAPPER_JAR%" (
  FOR /F "tokens=1,* delims==" %%A IN ('findstr /R /C:"^wrapperUrl=" "%WRAPPER_PROPERTIES%"') DO SET WRAPPER_URL=%%B
  IF EXIST "%ProgramFiles%\Git\usr\bin\curl.exe" (
    SET CURL="%ProgramFiles%\Git\usr\bin\curl.exe" -fsSL
    ECHO Downloading Maven Wrapper JAR from %WRAPPER_URL%
    %CURL% %WRAPPER_URL% -o "%WRAPPER_JAR%"
  ) ELSE (
    powershell -Command "& { $ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%' }"
  )
)

SET JAVA_EXE=java
"%JAVA_EXE%" -cp "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
ENDLOCAL
