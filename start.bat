echo off
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do set datetime=%%I
set datetime=%datetime:~0,8%-%datetime:~8,6%

set rootpath=%~dp0

cd data
mkdir %datetime%
cd ../

cd network-degree-distribution

echo on
call mvn clean compile exec:java -Dexec.args="%rootpath% %datetime%"

cd ../
python display_data.py %rootpath% %datetime%