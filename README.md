# 项目介绍

N34 Space是个人开发的微博系统，包括前端和后端两部分，基于Spring Boot + Spring Data JPA实现，采用Docker容器化部署。前端包括注册登录、微博浏览等模块。后端包括认证授权、微博管理等模块。

# 项目演示

## 注册界面

![image-20220228194122919](.\README.assets\image-20220228194122919.png)

## 登陆界面

![image-20220228194059745](.\README.assets\image-20220228194059745.png)

## 微博浏览界面

![image-20220228194234450](.\README.assets\image-20220228194234450.png)

# 技术选型

## 后端技术

| 技术            | 说明         | 官网                                                         |
| --------------- | ------------ | ------------------------------------------------------------ |
| Spring Boot     | 容器+MVC框架 | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| Spring Data JPA | ORM框架      | https://spring.io/projects/spring-data-jpa                   |
| HikariCP        | 数据库连接池 | https://github.com/brettwooldridge/HikariCP                  |
| JWT             | JWT登录支持  | https://jwt.io/                                              |
| MySQL           | 数据库       | https://www.mysql.com/                                       |

## 前端技术

| 技术       | 说明         | 官网                                     |
| ---------- | ------------ | ---------------------------------------- |
| Vue.js     | 前端框架     | [https://vuejs.org/](https://vuejs.org/) |
| Vue Router | 路由框架     | https://router.vuejs.org/                |
| Axios      | 前端HTTP框架 | https://axios-http.com/                  |
| Bootstrap  | 前端框架     | https://getbootstrap.com/                |

## 开发工具

| 工具          | 说明              | 官网                                |
| ------------- | ----------------- | ----------------------------------- |
| IntelliJ IDEA | Java开发IDE       | https://www.jetbrains.com/idea/     |
| MobaXterm     | Linux远程连接工具 | https://mobaxterm.mobatek.net/      |
| Postman       | API接口调试工具   | https://www.postman.com/            |
| Typora        | Markdown编辑器    | https://typora.io/                  |
| WebStorm      | 前端开发框架      | https://www.jetbrains.com/webstorm/ |

## 项目依赖

| 依赖         | 版本     |
| ------------ | -------- |
| JDK          | 16       |
| MySQL        | 8.0.27   |
| Nginx        | 1.10     |
| Docker       | 20.10.10 |
| Vue.js       | 2.6.11   |
| Apache Maven | 3.8.1    |

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