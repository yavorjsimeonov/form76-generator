FROM amazoncorretto:17-alpine-jdk
MAINTAINER yavor
ENV LANG en_GB.UTF-8
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

ADD ./src/main/resources/application.properties /app/application.properties
ADD ./src/main/resources/log4j2.properties /logs/log4j2.properties

EXPOSE 8080:8080
COPY ./build/libs/form76-generator-0.0.1-SNAPSHOT.jar form76-generator-1.0.0.jar
ENTRYPOINT ["java","-jar","form76-generator-1.0.0.jar","-Dapp.home=classpath:file:/app/", "-Dlog4j.configurationFile=classpath:file:/logs/log4j2.properties","--spring.profiles.active=default","--server.port=8080"]