FROM amazoncorretto:17-alpine-jdk
#FROM tomcat:9.0-alpine
MAINTAINER yavor
ENV LANG en_GB.UTF-8
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

ADD ./src/main/resources/application.properties /app/application.properties
ADD ./src/main/resources/nimble-theme-448917-u5-c5df7d240535.json /app/nimble-theme-448917-u5-c5df7d240535.json
ADD ./src/main/resources/log4j2.xml /logs/log4j2.xml
#ADD ./build/libs/form76-generator-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/form76.war

EXPOSE 8080:8080
COPY ./build/libs/server-1.0.0.jar server-1.0.0.jar
ENTRYPOINT ["java","-jar","server-1.0.0.jar","-Dapp.home=classpath:file:/app/", "-Dlog4j.configurationFile=classpath:file:/logs/log4j2.xml","--spring.profiles.active=default","--server.port=8080"]
#CMD ["catalina.sh", "run"]
