call mvn clean -Dmaven.test.skip install -P production
cd ./rxjrest-web
call mvn clean -Dmaven.test.skip package -P production
cd ../
pause