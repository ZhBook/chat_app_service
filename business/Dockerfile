FROM openjdk:21-oracle

MAINTAINER zhooke
WORKDIR /apps
ADD target/business.jar /apps/
EXPOSE 8001
VOLUME /apps/chat_app_service/business
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx128m","-Xms128m","business.jar","--spring.profiles.active=prod"]