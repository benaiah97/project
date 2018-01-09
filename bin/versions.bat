@setlocal
@REM Run customized version of maven-versions-plugin showing only out of date dependencies and plugins
@set OFFLINE= 
@set OPTS= 
@REM customized version which outputs a needed update only once for an entire project hierarchy
@set VERSIONS=org.codehaus.mojo:versions-maven-plugin:2.2.1-SNAPSHOT

call mvn %OFFLINE% %OPTS% %1 %2 %3 %VERSIONS%:display-plugin-updates %VERSIONS%:display-dependency-updates %VERSIONS%:display-property-updates -Dmaven.version.rules=file:/%M2_HOME%/conf/versions-rules.xml > versions.txt

@endlocal
