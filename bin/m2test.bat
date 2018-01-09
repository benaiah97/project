@setlocal

rmdir /S /Q target\cartridge-test
rmdir /S /Q target\src
rmdir /S /Q target\classes
del /S /F /Q target\*.jar

call mvn %1 %2 install org.apache.maven.plugins:maven-install-plugin:2.2:install source:jar -Dmaven.test.failure.ignore=true -DcreateChecksum=true -DupdateReleaseInfo=true

@REM mvn install:install-file -DpomFile=pom.xml -Dfile=target\*-sources.jar -Dclassifier=sources -DcreateChecksum=true

@REM call mvn eclipse:eclipse install org.apache.maven.plugins:maven-install-plugin:2.2:install source:jar -Dmaven.test.skip=true -DcreateChecksum=true -DupdateReleaseInfo=true -DdownloadSources=true

@endlocal
