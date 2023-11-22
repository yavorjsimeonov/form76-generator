FROM amazoncorretto:17-alpine-jdk
MAINTAINER yavor
EXPOSE 8080:8080
COPY ./build/libs/form76-generator-0.0.1-SNAPSHOT.jar form76-generator-1.0.0.jar
ENTRYPOINT ["java","-jar","form76-generator-1.0.0.jar","--spring.profiles.active=default","--server.port=8080"]