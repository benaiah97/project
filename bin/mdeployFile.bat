@REM Usage %1=groupId with /, %2=artifactId same name as source artifact jar name, %3=version, %4=artifact, %5=packaging

mvn deploy:deploy-file -DgroupId=%1 -DartifactId=%2 -Dversion=%3 -Dfile=%4.%5 -Dpackaging=%5 -DgeneratePom=true -DcreateChecksum=true -DgeneratePom.description="DREAMS RAR %2" -DrepositoryId=wdw -Durl=http://javacoe.wdw.disney.com/nexus/content/repositories/snapshots/ -DuniqueVersion=false
