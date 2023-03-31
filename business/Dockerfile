FROM openjdk:17.0.2-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/business.jar /apps/
EXPOSE 8001
VOLUME /apps/chat_app_service/business
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx256m","-Xms256m","business.jar","--spring.profiles.active=prod"]