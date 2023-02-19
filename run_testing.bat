echo off

rem This is purely used for testing, the code behind it will be changed constantly.

cd network-models
echo on
call mvn clean compile exec:java -Dexec.mainClass="com.lucaswarwick02.mains.TestMain"

cd ../