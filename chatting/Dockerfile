FROM openjdk:11.0.13-jdk

MAINTAINER zhooke
WORKDIR /apps
ADD target/chatting.jar /apps/
RUN echo "Asia/Shanghai" > /etc/timezone
VOLUME /apps/chat_app_service/chatting
EXPOSE 9001 58080
CMD ["java","-jar","-Xmx128m","-Xms128m","chatting.jar"]