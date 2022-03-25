## docker-compose.yml  服务器 部署脚本
mkdir -p /home/admin/application
tar zxvf /home/admin/app/package.tgz -C /home/admin/application/
sh /home/admin/application/startup.sh
docker-compose -f /home/admin/application/docker-compose.yml up -d
docker rmi $(docker images | grep "none" | awk '{print $3}')