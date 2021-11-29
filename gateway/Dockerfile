FROM openjdk:11.0.13-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/gateway.jar /apps/
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 8008
CMD ["java","-jar","-Xmx128m","-Xms128m","gateway.jar"]