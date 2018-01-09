@setlocal
@REM Run clean install with cobertura html output

call mvn clean > clean.txt
SHIFT
echo call mvn -e install %*> install.log 2>&1
@REM  -Dcobertura.report.format=html
call mvn -e install -Dcobertura.report.format=html -Dcobertura.skip=false %*> install.log 2>&1

@endlocal
