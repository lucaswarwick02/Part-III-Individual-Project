echo off

set rootpath=%~dp0

cd network-models
echo on
call mvn clean compile exec:java -Dexec.args="%rootpath% 2500 100"

cd ../
python analyze_data.py