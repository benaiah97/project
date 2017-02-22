###############################################################################
# tomcat-dtigateway
#  Note: This Dockerfile is not intended as an example of best practices,
#  it is only just good enough for local development deployment/debugging.
###############################################################################

FROM tomcat:7.0
MAINTAINER moons012 <shan.m.moon@disney.com>

###############################################################################
# SETUP NEEDED DIRECTORIES/VOLUMES
# NOTE WE COULD PULL THIS FROM A REPOSITORY
###############################################################################

RUN echo "Adding config directory so required properties are present."
ADD config/LOCAL/ /var/opt/apps/WDPRApps/DTI/CONFIG/Gateway/
RUN chmod -R 775 /var/opt/apps/WDPRApps/DTI/CONFIG

RUN echo "Creating log directory"
RUN mkdir -p /var/opt/apps/WDPRApps/DTI/LOGS/Gateway
RUN chmod -R 777 /var/opt/apps/WDPRApps/DTI/LOGS/Gateway

###############################################################################
# INSTALL REQUIRED CERTS
# JRE_HOME:        /usr
###############################################################################

RUN echo "Installing certs required to run gateway."
ADD docker/certs/ /var/opt/apps/WDPRApps/DTI/CERTS/
RUN chmod -R 775 /var/opt/apps/WDPRApps/DTI/CERTS/

RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/twdc-issuing-sha2_509.cer -alias twdc-issuing -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/twdc-root-sha2_509.cer -alias twdc-root -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 

RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/wm-flor-dv078.wdw.disney.com-1.cer -alias wm-flor-dv078.wdw.disney.com-1 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/wm-flor-dv078.wdw.disney.com-2.cer -alias wm-flor-dv078.wdw.disney.com-2 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/wm-flor-dv078.wdw.disney.com-3.cer -alias wm-flor-dv078.wdw.disney.com-3 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 

###############################################################################
# COPY DTI SCHEMAS
# NOTE: we could pull this from a repo and unzip
###############################################################################

RUN echo "Copying DTI SCHEMAS to /var/opt/apps/RT/schemas"
ADD docker/xsd/ /var/opt/apps/WDPRApps/RT/schemas/
ADD docker/xsd/ /var/opt/apps/RT/schemas/
RUN chmod -R 777 /var/opt/apps/WDPRApps/RT/schemas
RUN chmod -R 777  /var/opt/apps/RT/schemas

###############################################################################
# MODIFY TOMCAT AS NEEDED
# DEFAULT CATOMCAT ENVIROMENT FROM OFFICIAL TOMCAT 7 IMAGE
#CATALINA_BASE:   /usr/local/tomcat
# CATALINA_HOME:   /usr/local/tomcat
# CATALINA_TMPDIR: /usr/local/tomcat/temp
# JRE_HOME:        /usr
# CLASSPATH:       /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar
###############################################################################

RUN echo "Setting up debugging on port 8000"
ENV JPDA_ADDRESS="8000"
ENV JPDA_TRANSPORT="dt_socket"

EXPOSE 8080 8080
EXPOSE 8000 8000

ENTRYPOINT ["catalina.sh", "jpda", "run"]

RUN echo "Copying setenv.sh to Tomcat bin so CATALINA_OPTS set on startup"
ADD docker/tomcat/setenv.sh /usr/local/tomcat/bin/setenv.sh

RUN echo "Copying tomcat-users.xml to Tomcat conf for mng gui users/roles"
ADD docker/tomcat/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml


RUN echo "Copying catalina.properties to Tomcat conf for shared loader"
ADD docker/tomcat/catalina.properties /usr/local/tomcat/conf/catalina.properties

RUN echo "Copying context.xml to Tomcat conf for datasourece"
ADD docker/tomcat/context.xml /usr/local/tomcat/conf/context.xml

###############################################################################
# PUT DTI WAR IN TOMCAT WEBAPPS
# Assumes you have already built the war and it is present in the 
# gatewaywar/target directory
###############################################################################
RUN echo "PUTTING GATEWAY WAR INTO WEBAPPS."
ADD gatewaywar/target/DTIWeb.war /usr/local/tomcat/webapps
