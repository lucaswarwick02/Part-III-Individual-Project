echo off

rem This requires main.java to run a single stochasticSimulation function once, 
rem All of the values are hardcoded for it and therefore is used for testing/validating
rem as well as having a unique datetime folder format.

rem Generate a folder name for this run using datetime
set FOLDER=%time:~0,2%%time:~3,2%%time:~6,2%_%date:~-10,2%%date:~-7,2%%date:~-4,4%
set ROOTPATH=%~dp0

rem Move to the maven project and build/run
cd network-models
echo on
call mvn clean compile exec:java -Dexec.args="%ROOTPATH% %FOLDER%" -Dexec.mainClass="com.lucaswarwick02.mains.StochasticMain"

rem Move back to the root folder and generate the python plots
set ENVNAME=network-models
cd ../
conda run -n %ENVNAME% python python/analyze.py %ROOTPATH% %FOLDER%