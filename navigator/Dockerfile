FROM payara/server-full:6.2025.10-jdk17

ENV JVM_ARGS="-Xmx512m -XX:MaxRAMPercentage=75.0 -Xss512k"

COPY post-boot-commands.asadmin $CONFIG_DIR
COPY target/navigator-1.0.war $DEPLOY_DIR
