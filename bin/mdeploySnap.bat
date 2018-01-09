@REM Usage %1=groupId with /, %2=artifactId same name as source artifact jar name, %3=version

mvn deploy:deploy-file -Dfile=%M2_REPO%/%1/%2/%3-SNAPSHOT/%2-%3-SNAPSHOT.jar -DpomFile=%M2_REPO%/%1/%2/%3-SNAPSHOT/%2-%3-SNAPSHOT.pom -DrepositoryId=wdw -Durl=http://javacoe.wdw.disney.com/nexus/content/repositories/snapshots/ -DuniqueVersion=false

@REM To deploy a snapshot project normally targeted to a different repository, to the WDPR nexus:
@REM mvn deploy -DaltDeploymentRepository=wdw::default::http://javacoe.wdw.disney.com/nexus/content/repositories/snapshots/  -Dnexus.repository.url=http://javacoe.wdw.disney.com/nexus/content/repositories/snapshots/ -Dnexus.repository.id=wdw > deploy.txt 2>&1
