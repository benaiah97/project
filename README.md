maven
=====

**Configuration file(s)** for the maven settings, for [DTSS nexus](https://nexus.disney.com/nexus) and [WDPR nexus](http://javacoe.wdw.disney.com/nexus) repositories and servers. Also some useful command-line shortcuts. Private user settings (including passwords) should be in your home directory ${user.home}/.m2/settings.xml

To **install maven** locally:

The Java Development Kit (JDK) must be installed, JAVA_HOME environment variable set, and %JAVA_HOME%\bin in the PATH. The Sun JDK8 is preferred.

Note: download and unzip is no longer necessary - the binary files are included in the repository. Just set the environment variables.
- Updates to maven settings and scripts in github can be applied (merged automatically) through 'git pull' on the locally cloned repository.

Set the following **environment variables** (under Computer -> Properties -> Advanced System Settings -> Environment Variables):<br/>
**M2_HOME** = The directory where maven was unzipped<br/>
**PATH** = Add %M2_HOME%\bin<br/>
**M2_REPO** = (optional) The directory where maven artifacts will be downloaded, if you want somewhere other than the default ${user.home}/.m2/repository<br/>
**MAVEN_OPTS** = (optional) if you need to increase the maven memory settings (JDK7 or earlier), for large compilations/tests, or add custom compilation settings.

For Unix environments, use $environment values instead of %environment% values.

For 64 bit JDK7 on a machine with a large amount of memory (>= 8GB) where GC performance is critical, we recommend **MAVEN_OPTS**=
-Xmx2048m -XX:MaxPermSize=1024m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSClassUnloadingEnabled -XX:CompileCommand=exclude,org/apache/velocity/runtime/directive/Foreach,render

For JDK8 the MaxPermSize parameter is deprecated and ignored with a warning. Compilation memory sizing is determined dynamically based on the local machine memory and capabilities.

The maven\bin directory also contains a number of useful **command-line shortcuts** (batch files):

- **mcinstall** : run 'mvn clean' then 'mvn install' with outputs to clean.txt and install.log<br/>
- **m2cinstall** : run 'mvn -o clean' then 'mvn -o install' (offline mode)<br/>
- **mclean**/m2clean : run 'mvn clean' but also manually delete /target, *.log, clean.txt<br/>
- **installJar %1 %2 %3** : manually install a file to local M2_REPO, %1=groupId, %2=artifactId/jar, %3=version<br/>
- **installSources %1 %2 %3 %4** : manually install sources artifact to local M2_REPO, %1=groupId, %2=artifactId/jar, %3=version, %4=source file name<br/>
- **mdeploy**/m2deploy %1 %2 %3 : manually deploy a file to nexus, %1=groupId, %2=artifactId/jar, %3=version<br/>
- **beautify** : run andromda-beautifier-plugin, which converts all fully-qualified package.Class references to import statements<br/>
- **eff** : run 'mvn help:effective-pom' with output to eff.txt, for troubleshooting dependency/plugin version issues for the entire maven parent hierarchy<br/>
- **site** : run 'mvn site site:deploy' with output to site.txt<br/>
- **trees** : run 'mvn dependency:tree dependency:list' with output to tree.txt (to analyze dependencies and troubleshoot conflicts)<br/>
- **versions** : run the maven-versions-plugin, outputs to versions.txt - any dependency, plugin, or property version updates.
