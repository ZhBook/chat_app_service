FROM openjdk:11.0.13-jdk

WORKDIR /apps
ADD target/business.jar /apps/
EXPOSE 8001
RUN echo "Asia/Shanghai" > /etc/timezone
CMD ["java","-jar","-Xmx128m","-Xms128m","business.jar"]