FROM openjdk:8u292

LABEL MAINTAINER=Andy
WORKDIR /apps
ADD target/gateway.jar /apps/
EXPOSE 8008
CMD ["java","-jar","-Xmx128m","-Xms128m","gateway.jar"]