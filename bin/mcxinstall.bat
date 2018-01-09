@setlocal
@REM Cobertura coverage with local sonar

call mvn clean > clean.txt
SHIFT
echo call mvn -e install %*> install.log 2>&1
@REM  -Dcobertura.report.format=xml
call mvn -e install %*> install.log 2>&1
@REM call mvn -e install -Dcobertura.report.format=xml %*> install.log 2>&1
call mvn org.codehaus.mojo:sonar-maven-plugin:3.0.2:sonar -Dsonar.login=admin -Dsonar.password=admin -Plocal-sonar > sonar.txt

@endlocal
