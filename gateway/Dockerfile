FROM openjdk:11.0.13-jdk

WORKDIR /apps
ADD target/gateway.jar /apps/
EXPOSE 8008
CMD ["java","-jar","-Xmx128m","-Xms128m","gateway.jar"]