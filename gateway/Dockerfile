FROM openjdk:17.0.2-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/gateway.jar /apps/
RUN echo "Asia/Shanghai" > /etc/timezone
VOLUME /apps/chat_app_service/gateway
EXPOSE 8008
CMD ["java","-jar","-Xmx256m","-Xms256m","gateway.jar","--spring.profiles.active=prod"]