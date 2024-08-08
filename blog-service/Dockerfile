FROM openjdk:21-oracle

MAINTAINER zhooke
WORKDIR /apps
ADD target/blog-service.jar /apps/
EXPOSE 8090
VOLUME /apps/chat_app_service/blog-service
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx128m","-Xms128m","blog-service.jar","--spring.profiles.active=prod"]