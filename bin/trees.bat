@setlocal
@REM Run customized version of maven-versions-plugin showing only out of date dependencies and plugins
@set OFFLINE= 
@set OPTS= 
@set DEP=org.apache.maven.plugins:maven-dependency-plugin:2.8

call mvn %OFFLINE% %OPTS% %1 %2 %3 %DEP%:tree %DEP%:list -Dsort=true > tree.txt
@REM show in both group/artifact and classloader order, to troubleshoot conflicts
call mvn %OFFLINE% %OPTS% %1 %2 %3 %DEP%:list >> tree.txt

@endlocal
