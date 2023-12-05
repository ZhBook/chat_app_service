FROM openjdk:17.0.2-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/admin.jar /apps/
EXPOSE 9110
VOLUME /apps/chat_app_service/admin
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx128m","-Xms128m","admin.jar","--spring.profiles.active=prod"]