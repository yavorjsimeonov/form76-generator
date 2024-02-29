#/bin/bash
docker-compose stop
docker-compose rm -f
docker image rm --force form76-generator:latest
gradle clean bootJar
docker-compose up -d