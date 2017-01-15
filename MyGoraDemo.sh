#!/bin/bash
echo "--------MyGoraDemo.jar start-------" 

classpath=""
for jarpath in `ls lib/*.jar` 
do 
classpath=${classpath}':/home/'${jarpath}
done
classpath="."${classpath}

java -cp ${classpath}:./MyGoraDemo.jar org.apache.gora.tutorial.log.UrlInfoManager -query all

echo "--------MyGoraDemo.jar end-------"