@setlocal
@REM Same as m2cinstall without -o

call mvn clean > clean.txt
SHIFT
echo call mvn -e install %*> install.log 2>&1
call mvn -e install %*> install.log 2>&1

@endlocal
