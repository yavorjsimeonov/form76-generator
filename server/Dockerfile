FROM amazoncorretto:17-alpine-jdk
#FROM tomcat:9.0-alpine
MAINTAINER yavor
ENV LANG en_GB.UTF-8
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

ADD ./src/main/resources/*.* /app/

EXPOSE 8080:8080
COPY ./build/libs/server-1.0.0.jar server-1.0.0.jar
ENTRYPOINT ["java","-jar","server-1.0.0.jar","-Dapp.home=classpath:file:/app/", "-Dlog4j.configurationFile=classpath:file:/logs/log4j2.xml","--spring.profiles.active=default","--server.port=8080"]
#CMD ["catalina.sh", "run"]
