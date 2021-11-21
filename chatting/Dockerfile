FROM openjdk:8u292

LABEL MAINTAINER=Andy
WORKDIR /apps
ADD target/chatting.jar /apps/
EXPOSE 9001 58080
CMD ["java","-jar","-Xmx128m","-Xms128m","chatting.jar"]