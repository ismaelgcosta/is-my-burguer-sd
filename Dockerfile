FROM maven:3.9.5-eclipse-temurin-21-alpine as build
#
WORKDIR /is-my-burguer-sd
#
COPY ./ ./
RUN mvn clean
RUN mvn install

FROM eclipse-temurin:21-jdk-alpine as main
EXPOSE 80
EXPOSE 443
EXPOSE 5005

COPY --from=build /is-my-burguer-sd/src/main/resources/springboot.crt springboot.crt
RUN keytool -importcert -file springboot.crt -alias springboot -keystore $JDK_HOME/jre/lib/security/cacerts
COPY --from=build /is-my-burguer-sd/target/is-my-burguer-sd.jar is-my-burguer-sd.jar

ENTRYPOINT ["java","-jar","is-my-burguer-sd.jar","--server.port=443","-Dspring.profiles.active=production"]
#CMD ["sleep","infinity"] Only for testing