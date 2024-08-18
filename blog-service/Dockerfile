FROM openjdk:21-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/blog-service.jar /apps/
EXPOSE 8001
VOLUME /apps/chat_app_service/blog-service
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx256m","-Xms256m","blog-service.jar","--spring.profiles.active=prod"]