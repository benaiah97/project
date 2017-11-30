###############################################################################
# tomcat-dtigateway
#  Note: This Dockerfile is not intended as an example of best practices,
#  it is only just good enough for local development deployment/debugging.
###############################################################################

FROM tomcat:7.0.63
ARG CONFIG=LOCAL
ARG TIMEZONE=America/New_York
#OTHER POSSIBLE VALUES Asia/Hong_Kong

MAINTAINER moons012 <shan.m.moon@disney.com>

###############################################################################
# SETUP NEEDED DIRECTORIES/VOLUMES
# NOTE WE COULD PULL THIS FROM A REPOSITORY
###############################################################################

RUN echo "Adding config directory so required properties are present."
RUN mkdir -p /var/opt/apps/WDPRApps/DTI/CONFIG/Gateway
add config/$CONFIG/ /var/opt/apps/WDPRApps/DTI/CONFIG/Gateway

RUN echo "Creating log directory"
#RUN mkdir -p /var/opt/apps/WDPRApps/DTI/gateway/logs/
#RUN chmod -R 777 /var/opt/apps/WDPRApps/DTI/gateway/logs
RUN mkdir -p /var/opt/apps/WDPRApps/DTI/LOGS/Gateway
RUN chmod -R 777 /var/opt/apps/WDPRApps/DTI/LOGS/Gateway

# Set the timezone.
RUN echo "Setting the timezone"
RUN echo "$TIMEZONE" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata


###############################################################################
# INSTALL REQUIRED CERTS
# JRE_HOME:        /usr
###############################################################################

RUN echo “setting up install of certs”

ADD docker/certs/ /var/opt/apps/WDPRApps/DTI/CERTS/
RUN chmod -R 777 /var/opt/apps/WDPRApps/DTI/CERTS/

#RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/twdc-issuing-sha2_509.cer -alias twdc-issuing -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 
#RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/twdc-root-sha2_509.cer -alias twdc-root -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 

RUN echo “Installing TWDC certs required to run gateway.”

RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/root.pem -alias twdc-root -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/root2.pem -alias twdc-root2 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/sub.pem -alias twdc-sub -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/sub2.pem -alias twdc-sub2  -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt

RUN echo “Installing HK certs required to run gateway.”

RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/entrust-root.cer -alias hk-entrust-root  -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/entrust-lvl1.cer -alias hk-entrust-l1  -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt
#RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/iago-latest-apac-wdpr-disney-com.pem -alias iago-latest-apac  -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt

RUN echo “Installing Domestic ATS Lab certs.”

RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/wm-flor-dv078.wdw.disney.com-1.cer -alias wm-flor-dv078.wdw.disney.com-1 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/wm-flor-dv078.wdw.disney.com-2.cer -alias wm-flor-dv078.wdw.disney.com-2 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 
RUN /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/keytool -import -file /var/opt/apps/WDPRApps/DTI/CERTS/wm-flor-dv078.wdw.disney.com-3.cer -alias wm-flor-dv078.wdw.disney.com-3 -keystore /usr/lib/jvm/java-7-openjdk-amd64/jre/lib/security/jssecacerts -storepass changeit -noprompt 

###############################################################################
# LINUX ADDITIONS FOR DEVELOPER CONVENIENCE
###############################################################################
RUN echo "Adding vim for developer convenience"
RUN apt-get update
RUN apt-get install -y  vim

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
# CATALINA_BASE:   /usr/local/tomcat
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

RUN echo "Copying setenv.sh to Tomcat bin so CATALINA_OPTS set on startup: " $CONFIG
ADD docker/tomcat/$CONFIG/setenv.sh /usr/local/tomcat/bin/setenv.sh

RUN echo "Copying tomcat-users.xml to Tomcat conf for mng gui users/roles"
ADD docker/tomcat/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

RUN echo "Copying catalina.properties to Tomcat conf for shared loader"
ADD docker/tomcat/catalina.properties /usr/local/tomcat/conf/catalina.properties

RUN echo "Copying context.xml to Tomcat conf for datasourece"
ADD docker/tomcat/$CONFIG/context.xml /usr/local/tomcat/conf/context.xml

###############################################################################
# PUT DTI WAR IN TOMCAT WEBAPPS
# Assumes you have already built the war and it is present in the 
# gatewaywar/target directory
###############################################################################
RUN echo "PUTTING GATEWAY WAR and ADM WAR INTO WEBAPPS."
COPY gatewaywar/target/DTIWeb.war /usr/local/tomcat/webapps/DTIApp.war

#DISABLING THE GATEWAYADM JAR
#COPY gatewayadm/target/SharedWeb.war /usr/local/tomcat/webapps/SharedWeb.war
