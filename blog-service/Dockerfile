FROM openjdk:21-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/blog-service-1.0.0-SNAPSHOT.jar /apps/
EXPOSE 8001
VOLUME /apps/blog-service
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx256m","-Xms256m","blog-service-1.0.0-SNAPSHOT.jar","--spring.profiles.active=prod"]