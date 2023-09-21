FROM openjdk:21-oracle

MAINTAINER zhooke
WORKDIR /apps
ADD target/gateway.jar /apps/
RUN echo "Asia/Shanghai" > /etc/timezone
VOLUME /apps/chat_app_service/gateway
EXPOSE 8008
CMD ["java","-jar","-Xmx128m","-Xms128m","gateway.jar","--spring.profiles.active=prod"]