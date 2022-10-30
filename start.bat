cd network-degree-distribution

call mvn clean compile exec:java

cd ../
python display_data.py