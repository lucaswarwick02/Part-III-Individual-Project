echo off

set run=%time:~0,2%%time:~3,2%%time:~6,2%_%date:~-10,2%%date:~-7,2%%date:~-4,4%
set rootpath=%~dp0

cd network-models
echo on
call mvn clean compile exec:java -Dexec.args="%rootpath% %run%"

cd ../
python python/analyze.py %rootpath% %run%