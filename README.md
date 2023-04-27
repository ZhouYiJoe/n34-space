# 环境配置

在启动项目前，需要配置好如下环境。

| 环境         | 版本                         |
| ------------ | ---------------------------- |
| JRE          | 8                            |
| MySQL        | 5.7.38                       |
| Angular      | 14.2.0                       |
| Apache Maven | 3.8.1                        |
| Redis        | 3.0.504                      |
| MinIO        | RELEASE.2022-11-29T23-40-49Z |
| Python       | 3.7.9                        |
| Flask        | 2.2.2                        |

# 启动方法

项目的所有代码都位于源代码目录`n34-project`下。

项目启动的步骤如下：

- 启动MySQL，创建数据库`n34`，执行`n34-project/backend/db/n34.sql`创建数据表。将root用户的密码设置为123456。
- 启动Redis。
- 启动MinIO。启动时带上参数`--console-address ":9010" --address ":9090"`。
- 在`n34-project/backend`下执行命令`mvn spring-boot:run`，启动后端。
- 在`n34-project/frontend`下执行命令`ng serve`，启动前端。
- 在`n34-project/py`下执行命令`python main.py`，启动微博内容筛选系统。

然后访问http://localhost:4200/，即可打开微博网站的主页。

# 系统操作指南

系统的具体操作方法请见其他附件中的演示视频。