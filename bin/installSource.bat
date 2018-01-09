@REM RJF 2/19/08 Wrapper for maven install-file task. Uses an existing source file,
@REM creates a POM and checksum, and adds to local maven repository.
@REM creates/Adds directory %M2_REPO%/group/artifact/artifact-version-sources.jar
@REM Usage: installSource <group> <artifact> <artifact qualifier> <version>
@REM Example: installSource com.disney.shared SharedCore 2.0.4-SNAPSHOT Src.zip
@REM creates the following files:
@REM com\disney\shared\SharedCore\2.0.4-SNAPSHOT\SharedCore-2.0.4-SNAPSHOT.jar,
@REM 

@setlocal

@echo mvn org.apache.maven.plugins:maven-install-plugin:2.2:install-file -Dfile=%2%4 -DgroupId=%1 -DartifactId=%2 -Dversion=%3 -Dpackaging=jar -Dclassifier=sources -DgeneratePom=true -DcreateChecksum=true

call mvn org.apache.maven.plugins:maven-install-plugin:2.2:install-file -Dfile=%2%4 -DgroupId=%1 -DartifactId=%2 -Dversion=%3 -Dpackaging=jar -Dclassifier=sources -DgeneratePom=true -DcreateChecksum=true

@endlocal
pause
