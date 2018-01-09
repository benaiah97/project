@REM Assumes project pom.xml has been updated with deployment location, i.e.
@REM    <distributionManagement>
@REM        <repository>
@REM           <id>internal</id>
@REM           <name>Internal WDW Repository</name>
@REM           <url>file:I:/AndroMDA/internal</url>
@REM           <uniqueVersion>false</uniqueVersion>
@REM        </repository>
@REM        <site>
@REM            <id>website</id>
@REM           <url>http://wm-flor-apvm004:81/internal</url>
@REM        </site>
@REM    </distributionManagement>


@setlocal

@REM rmdir /S /Q target\classes
@REM del /S /F /Q target\*.jar

@REM mvn -U deploy org.apache.maven.plugins:maven-install-plugin:install source:jar -Dmaven.test.skip=true -DcreateChecksum=true -DupdateReleaseInfo=true -DaltDeploymentRepository=internal::default::file:I:/AndroMDA/internal
@REM xcopy %M2_REPO%\org\andromda\*-sources.jar I:\AndroMDA\internal\org\andromda /D /P /S /F /R

@REM Usage %1=groupId with /, %2=artifactId same name as source artifact jar name, %3=version, %4=subdirectory with jar

mvn deploy:deploy-file -Dfile=%M2_REPO%/%1/%2/%3/%2-%3.jar -DpomFile=%M2_REPO%/%1/%2/%3/%2-%3.pom -DrepositoryId=wdw -Durl=http://javacoe.wdw.disney.com/nexus/content/repositories/releases/ -DuniqueVersion=false

@REM Cannot deploy an artifact from the local repo that already exists in the local repo, must rename the directory.
@REM mvn -e deploy:deploy-file -Dfile=%M2_REPO%/com/wdw/dvc/webservice_client/1.1-SNAPSHOT/webservice_client-1.1-SNAPSHOT.jar -DpomFile=%M2_REPO%/com/wdw/dvc/webservice_client/1.1-SNAPSHOT/webservice_client-1.1-SNAPSHOT.pom -DrepositoryId=wdw -Durl=http://javacoe.wdw.disney.com/nexus/content/repositories/snapshots/  -DuniqueVersion=false

@endlocal
