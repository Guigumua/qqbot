# qqbot

基于CQhttp和simple-robot开发的qqbot

> 本机器人的多数功能需要发送图片，而coolq air不能发送图片，如果可能请安装coolq pro，如果不能，那我也没办法。

### 快速开始

#### linux

```shell
# 安装docker
curl https://get.docker.com | sh
# 下载docker-compose
curl -L https://github.com/docker/compose/releases/download/1.25.4/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
# 如果上面下载太慢，可以在github上访问docker-compo的repository，在releases里下载
mkdir /usr/local/docker
cd /usr/local/docker
vim docker-compose.yml
```

```yaml
version: "3.7"
services:
  cqhttp:
    image: richardchien/cqhttp:latest
    container_name: cqhttp
    ports:
      - "9000:9000"  # noVNC 管理
      - "127.0.0.1:5700:5700"  # CQHTTP 默认的 HTTP 端口
      - "127.0.0.1:6700:6700"  # CQHTTP 默认的 WebSocket 端口
    restart: always  # 重启策略
    volumes:
      - ./coolq/:/home/user/coolq/  # 挂载 酷Q 主目录
    environment:
      VNC_PASSWD: 12345678  # noVNC 连接密码
      COOLQ_ACCOUNT: 12345678  # 要登录的机器人 QQ 号
      COOLQ_URL: http://dlsec.cqp.me/cqp-tuling  # 如果使用 酷Q Air，本行可以删除，如使用 酷Q Pro，需将 URL 中的 cqa-tuling 改为 cqp-tuling
      FORCE_ENV: "true"  # 强制使用环境变量中的配置（下面以 CQHTTP_ 开头的那些），这会使手动对 CQHTTP 配置文件的修改在重启容器后失效，删除此项则下面的配置只在容器第一次启动时写入配>置文件
      CQHTTP_POST_URL: http://172.17.0.1:80  # HTTP 上报地址，172.17.0.1 通常是宿主机在默认网桥上的 IP 地址，可通过 docker inspect bridge 查看
      CQHTTP_SERVE_DATA_FILES: "true"
      CQHTTP_USE_WS: "true"  
  mysql:
    image: mysql
    command: "--default-authentication-plugin=mysql_native_password --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --secure_file_priv=/var/lib/mysql"
    restart: always
    privileged: true
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: qqbot
    ports:
      - 3306:3306
    volumes:
      - /usr/local/docker/mysql/conf/my.cnf:/etc/mysql/my.cnf
      - /usr/local/docker/mysql/conf.d/:/etc/mysql/conf.d/
      - /usr/local/docker/mysql/data/:/var/lib/mysql/
```

```shell
docker-compose up -d
```

完成后访问你的服务器ip:port输入vnc密码后登陆机器人账号。

```shell
cd /usr/local
git clone https://github.com/Guigumua/qqbot.git
cd qqbot
# 修改数据连接配置和qq机器人账号
vim src/mian/resources/application.yml
mvn package
cd target
java -jar qqbot-0.0.1-SNAPSHOT.jar
```

#### windows

在coolq的官网下载解压coolqpro后，[请参照本文档安装coolqhttp插件](https://cqhttp.cc/docs/4.14/#/)，注意coolq的解压路径需要是`D:/software/coolq/`，如果你的路径是其他的，请修改`qqbot.utils.Constants.SAVE_PATH`，这个常量决定文件的保存位置。

### 已有功能

#### pixiv

p站相关功能，现支持关键字搜图，pid搜图，以图搜详细信息。

> `pixiv keyword`：回复一张包含keyword的图片，例：`pixiv 碧蓝航线`
>
> `pixiv pid`：通过pid查询一张图片的详细信息，回复一张图片
>
> `pixiv image`：image是一张图片，回复图片的详细信息，包含pid，url，member，title

#### image

图片上传和查看

> img tags images：tags是你要上传的图片的标签，images是你要上传的图片，支持多个标签和多张图片，回复图片的保存信息
>
> img tag：查看一张有标签为tag的图片，回复一张图片

#### setu

> setu：回复一张setu

#### 其他

帮助：使用help查看帮助信息

复读：默认开启，监听被重复发过的消息

### TODO

- [ ] 功能开关和权限
- [ ] 群事件推送
- [ ] 以图搜番
- [ ] 壁纸，一言
- [ ] 碧蓝航线wiki
- [ ] 碧蓝航线模拟建造

