FROM openjdk:21-oracle

MAINTAINER zhooke
WORKDIR /apps
ADD target/oauth.jar /apps/
RUN echo "Asia/Shanghai" > /etc/timezone
VOLUME /apps/chat_app_service/oauth
EXPOSE 8002
CMD ["java","-jar","-Xmx128m","-Xms128m","oauth.jar","--spring.profiles.active=prod"]
# docker run --name micro-user \
# -e spring_cloud_nacos_server_addr=10.0.0.12:8848 \
# -e micro_datasource_ip=10.0.0.12 \
# -e micro_datasource_username=root \
# -e micro_datasource_password=redhat \
# -e spring_redis_host=10.0.0.12 \
# -d micro-user:4.5
