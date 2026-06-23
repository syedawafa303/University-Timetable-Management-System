@echo off
setlocal enabledelayedexpansion
title University Timetable Management System Launcher

echo =================================================================
echo        University Timetable Management System Launcher
echo =================================================================
echo.

:: Create folders
if not exist lib mkdir lib
if not exist bin mkdir bin

:: Check if Java is installed globally
where java >nul 2>nul
if !ERRORLEVEL! equ 0 (
    set JAVA_CMD=java
    set JAVAC_CMD=javac
    echo [INFO] Found global Java installation.
) else (
    :: Check if local JDK already exists
    if exist local-jdk\bin\javac.exe (
        set JAVA_CMD=local-jdk\bin\java.exe
        set JAVAC_CMD=local-jdk\bin\javac.exe
        echo [INFO] Found local JDK installation.
    ) else (
        echo [WARNING] Java Development Kit JDK was not found on your system.
        echo [INFO] Downloading portable JDK 21 OpenJDK to run this project...
        echo [INFO] This might take a few minutes depending on your internet speed. Please wait...
        echo.

        set JDK_ZIP=jdk.zip
        set JDK_URL=https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.3%%2B9/OpenJDK21U-jdk_x64_windows_hotspot_21.0.3_9.zip

        powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; echo 'Downloading JDK...'; Invoke-WebRequest -Uri '!JDK_URL!' -OutFile '!JDK_ZIP!'"
        if !ERRORLEVEL! neq 0 (
            echo [ERROR] Failed to download JDK. Please check your internet connection or install Java manually.
            pause
            exit /b 1
        )

        echo [INFO] Extracting JDK...
        powershell -Command "Expand-Archive -Path '!JDK_ZIP!' -DestinationPath 'jdk_temp'"
        
        :: Move extracted folder contents to local-jdk
        move jdk_temp\jdk-21.0.3+9 local-jdk
        
        :: Clean up temporary files
        del !JDK_ZIP!
        rmdir /s /q jdk_temp

        if exist local-jdk\bin\javac.exe (
            set JAVA_CMD=local-jdk\bin\java.exe
            set JAVAC_CMD=local-jdk\bin\javac.exe
            echo [SUCCESS] Local JDK set up successfully.
        ) else (
            echo [ERROR] JDK extraction failed. Please try running as Administrator.
            pause
            exit /b 1
        )
    )
)

:: Download SQLite JDBC driver and SLF4J dependencies if they don't exist
set SQLITE_JAR=lib\sqlite-jdbc-3.45.1.0.jar
set SLF4J_API_JAR=lib\slf4j-api-2.0.9.jar
set SLF4J_SIMPLE_JAR=lib\slf4j-simple-2.0.9.jar
set FLATLAF_JAR=lib\flatlaf-3.4.1.jar

if not exist "!SQLITE_JAR!" (
    echo [INFO] Downloading SQLite JDBC driver...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.1.0/sqlite-jdbc-3.45.1.0.jar' -OutFile '!SQLITE_JAR!'"
)

if not exist "!SLF4J_API_JAR!" (
    echo [INFO] Downloading SLF4J API dependency...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar' -OutFile '!SLF4J_API_JAR!'"
)

if not exist "!SLF4J_SIMPLE_JAR!" (
    echo [INFO] Downloading SLF4J Simple Logger dependency...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar' -OutFile '!SLF4J_SIMPLE_JAR!'"
)

if not exist "!FLATLAF_JAR!" (
    echo [INFO] Downloading FlatLaf Look and Feel dependency...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar' -OutFile '!FLATLAF_JAR!'"
)

set CP_LIBS=!SQLITE_JAR!;!SLF4J_API_JAR!;!SLF4J_SIMPLE_JAR!;!FLATLAF_JAR!

:: Compile java files
echo.
echo [INFO] Compiling Java source files...

:: Search for all Java source files
powershell -Command "Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName.Replace($pwd.Path + '\', '') } | Out-File -FilePath sources.txt -Encoding ascii"
"!JAVAC_CMD!" -encoding UTF-8 -cp "!CP_LIBS!" -d bin @sources.txt
set COMPILE_STATUS=!ERRORLEVEL!
del sources.txt

if !COMPILE_STATUS! neq 0 (
    echo.
    echo [ERROR] Compilation failed.
    pause
    exit /b !COMPILE_STATUS!
)

echo [SUCCESS] Compilation completed successfully.
echo.
echo [INFO] Launching application...
"!JAVA_CMD!" -cp "bin;!CP_LIBS!" Main

if !ERRORLEVEL! neq 0 (
    echo.
    echo [ERROR] Execution failed or interrupted.
)
echo.
pause
