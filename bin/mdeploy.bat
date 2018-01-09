@REM Usage %1=groupId with /, %2=artifactId same name as source artifact jar name, %3=version

mvn deploy:deploy-file -Dfile=%M2_REPO%/%1/%2/%3/%2-%3.jar -DpomFile=%M2_REPO%/%1/%2/%3/%2-%3.pom -DrepositoryId=wdw -Durl=http://javacoe.wdw.disney.com/nexus/content/repositories/releases/ -DuniqueVersion=false

@REM To deploy a snapshot project normally targeted to a different repository, to the WDPR nexus:
@REM mvn deploy -DaltDeploymentRepository=wdw::default::http://javacoe.wdw.disney.com/nexus/content/repositories/snapshots/ > deploy.txt
