# 项目描述
## 基本信息
- 项目名称：chat_app
- 目的：学习SpringCloud
- 主要功能：包含聊天、blog、photo等功能

## 使用场景
- 环境：JDK21
- 依赖：SpringCloud 2022.0.4、SpringBoot 3.1.5
- 数据库：MySQL
- 技术：JAVA

## 安装部署
docker-compose.yml  服务器 部署脚本
mkdir -p /home/admin/application
tar zxvf /home/admin/app/package.tgz -C /home/admin/application/
sh /home/admin/application/startup.sh
docker-compose -f /home/admin/application/docker-compose.yml up -d
docker rmi $(docker images | grep "none" | awk '{print $3}')