@echo off
setlocal

set WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=.mvn\wrapper\maven-wrapper.properties

if not exist %WRAPPER_JAR% (
    echo Maven Wrapper JAR not found.
    exit /b 1
)

REM Read distribution URL from properties file
for /f "tokens=2 delims==" %%a in ('findstr /i "distributionUrl" %WRAPPER_PROPERTIES%') do set DISTRIBUTION_URL=%%a

REM Set MAVEN_HOME
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.8.6-bin\apache-maven-3.8.6

if not exist %MAVEN_HOME% (
    echo Downloading Maven...
    if not exist %USERPROFILE%\.m2\wrapper\dists mkdir %USERPROFILE%\.m2\wrapper\dists
    if not exist %USERPROFILE%\.m2\wrapper\dists\apache-maven-3.8.6-bin mkdir %USERPROFILE%\.m2\wrapper\dists\apache-maven-3.8.6-bin
    
    echo Downloading from %DISTRIBUTION_URL%...
    powershell -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.8.6-bin\apache-maven-3.8.6-bin.zip'"
    
    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.8.6-bin\apache-maven-3.8.6-bin.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.8.6-bin'"
)

REM Set MAVEN_OPTS
set MAVEN_OPTS=-Dmaven.multiModuleProjectDirectory=.

REM Run Maven
set MAVEN_EXEC=%MAVEN_HOME%\bin\mvn.cmd
if not exist %MAVEN_EXEC% (
    echo Maven executable not found at %MAVEN_EXEC%
    exit /b 1
)

call %MAVEN_EXEC% %*