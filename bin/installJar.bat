@REM RJF 2/19/08 Wrapper for maven install-file task. Uses an existing jar,
@REM creates a POM and checksum, and adds to local maven repository.
@REM creates/Adds directory %M2_REPO%/group/artifact/artifact-version.jar
@REM Usage: installJar <group> <jar name> <version>
@REM Example: installJar com.disney.shared SharedCore 2.0.4-SNAPSHOT
@REM creates the following files:
@REM com\disney\shared\SharedCore\2.0.4-SNAPSHOT\SharedCore-2.0.4-SNAPSHOT.jar,
@REM 

@setlocal

mvn install:install-file -Dfile=%2.jar -DgroupId=%1 -DartifactId=%2 -Dversion=%3 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true

@endlocal
pause
