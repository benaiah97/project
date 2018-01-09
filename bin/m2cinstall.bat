@setlocal

call mvn -o clean > clean.txt
call mvn -o -e install %1 > install.log

@endlocal
