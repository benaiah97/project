@setlocal

call mvn %1 %2 clean

del /S /F /Q *.log
del /S /F /Q *.exc

@endlocal
