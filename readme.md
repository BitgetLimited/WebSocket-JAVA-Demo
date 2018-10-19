## 环境要求
jdk 1.8

## 运行  

1.先修改application.properties中的accessKey，secretKey

cd 项目路径

mvn clean package -Dmaven.test.skip=true

cd target/

java -jar WebSocket-JAVA-demo-1.jar  




# application.properties参数说明
| 名称| 说明|
|----|----|
|accessKey |用户accessKey|
|    secretKey | 用户secretKey|
|   uri.host      | websocket地址|
|   uri.protocol  | 协议|
|    uri.ao.path     |  账户及订单请求路径|
|    uri.market.path     |  账户及订单请求路径|
|    uri.port     |  端口号|


# 相关说明
项目中包含，行情以及订单账户的websocket连接demo

