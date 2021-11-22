FROM openjdk:11.0.13-jdk

WORKDIR /apps
ADD target/business.jar /apps/
EXPOSE 8001
CMD ["java","-jar","-Xmx128m","-Xms128m","business.jar"]