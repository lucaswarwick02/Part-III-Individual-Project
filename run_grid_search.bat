echo off

rem This requires main.java to run the `gridSearchSimulations` function which iterates through
rem the different paremeters for the vaccination strategies. Outputs a datetime folder for the
rem run, but within that are the individual runs.

rem Generate a folder name for these runs using datetime
set FOLDER=%time:~0,2%%time:~3,2%%time:~6,2%_%date:~-10,2%%date:~-7,2%%date:~-4,4%
set ROOTPATH=%~dp0

rem Move to the maven project and build/run
cd network-models
echo on
call mvn clean compile exec:java -Dexec.args="%ROOTPATH% %FOLDER%" -Dexec.mainClass="com.lucaswarwick02.mains.GridSearchMain"