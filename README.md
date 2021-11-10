# 部署方法

把仓库中的docker目录复制到服务器上

把后端项目和前端项目都打包(也就是获得Spring Boot项目的jar包和Vue.js项目的dist)

把jar包放到docker/backend下，把dist放到docker/frontend下

修改docker/nginx/nginx-conf/nginx.conf，把里面的localhost改为服务器的IP地址

然后在docker目录下运行

```shell
docker-compose up
```

待容器全部启动后，进入mysql容器中，创建名为n34的数据库

然后项目即可正常运行

服务器的IP地址即为项目的访问地址