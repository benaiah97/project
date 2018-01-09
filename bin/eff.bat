@REM RJF 8/21/13 Shortcut for effective-pom
@REM 

@setlocal

call mvn help:effective-pom %1 %2 %3 > eff.txt

@endlocal
