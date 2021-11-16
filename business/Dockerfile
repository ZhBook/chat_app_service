FROM openjdk:8u292

LABEL MAINTAINER=Andy
WORKDIR /apps
ADD target/business.jar /apps/
CMD ["java","-jar","-Xmx128m","-Xms128m","business.jar"]