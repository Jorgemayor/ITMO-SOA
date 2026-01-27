FROM payara/server-full:6.2025.10-jdk17

ENV JVM_ARGS="-Xmx256m -XX:MaxRAMPercentage=75.0 -Xss256k"
ENV _JAVA_OPTIONS="-Xss256k"

COPY post-boot-commands.asadmin $CONFIG_DIR
COPY target/navigator-1.0.war $DEPLOY_DIR
