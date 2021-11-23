FROM openjdk:11.0.13-jdk

WORKDIR /apps
ADD target/chatting.jar /apps/
RUN echo "Asia/Shanghai" > /etc/timezone
EXPOSE 9001 58080
CMD ["java","-jar","-Xmx128m","-Xms128m","chatting.jar"]