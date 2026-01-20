FROM payara/server-full:6.2025.10-jdk17

# Copy the WAR file
COPY target/navigator-1.0.war $DEPLOY_DIR

# Generate self-signed certificate during build
RUN keytool -genkeypair \
    -alias payara-selfsigned \
    -keyalg RSA \
    -keysize 2048 \
    -storetype PKCS12 \
    -keystore /opt/payara/appserver/glassfish/domains/domain1/config/keystore.jks \
    -validity 365 \
    -storepass changeit \
    -keypass changeit \
    -dname "CN=localhost, OU=SOA, O=ITMO, L=Saint-Petersburg, S=Saint-Petersburg, C=RU"

# Copy post-boot commands to configure SSL on startup
COPY post-boot-commands.asadmin /opt/payara/scripts/post-boot-commands.asadmin
