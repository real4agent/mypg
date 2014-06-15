@echo off
echo [INFO] Install pangoo.
cd %~dp0
cd ..
REM call mvn clean install -pl . -Dmaven.test.skip=true
call mvn clean install -Dmaven.test.skip=true
REM echo [INFO] Install pangoo :: modules.
REM cd %~dp0
REM cd ../modules
REM call mvn clean install -pl . -Dmaven.test.skip=true


REM echo [INFO] Install common.
REM cd %~dp0
REM cd ../common
REM call mvn clean install -pl .  -Dmaven.test.skip=true

cd bin
pause