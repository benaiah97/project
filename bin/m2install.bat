@setlocal

rmdir /S /Q target\classes
del /S /F /Q target\*.jar

call mvn -o %1 %2 install source:jar -DcreateChecksum=true -DupdateReleaseInfo=true > install.log

@REM call mvn -o %1 %2 install source:jar -Dmaven.test.skip=true -DcreateChecksum=true -DupdateReleaseInfo=true > install.log

@REM mvn install:install-file -DpomFile=pom.xml -Dfile=target\*-sources.jar -Dclassifier=sources -DcreateChecksum=true

@REM call mvn eclipse:eclipse install org.apache.maven.plugins:maven-install-plugin:2.2:install source:jar -Dmaven.test.skip=true -DcreateChecksum=true -DupdateReleaseInfo=true -DdownloadSources=true

@endlocal
