@echo off
echo ==================================================
echo ⚡ FastSharedMemory Inter-Process Benchmark
echo ==================================================
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven build failed!
    exit /b %ERRORLEVEL%
)
echo.
echo Launching JMH Zero-Copy IPC Benchmark...
java --enable-native-access=ALL-UNNAMED -jar target/benchmarks.jar
pause
