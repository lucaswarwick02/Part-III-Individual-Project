set filename=model_%time:~0,2%%time:~3,2%%time:~6,2%_%date:~-10,2%%date:~-7,2%%date:~-4,4%.csv
set rootpath=%~dp0

set "java_args=%rootpath% %filename%"

cd network-degree-distribution

call mvn clean compile exec:java -Dexec.args="%java_args%"

cd ../
python display_data.py %rootpath% %filename%