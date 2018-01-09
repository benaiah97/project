@setlocal

@REM rmdir /S /Q target\classes

call mvn %1 %2 site site:deploy -Plocal,andromda-site-light  > site.txt

@endlocal
