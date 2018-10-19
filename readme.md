## 环境要求
jdk 1.8

## 运行  

1. 

cd 项目路径

mvn clean package -Dmaven.test.skip=true

cd target/

java -jar WebSocket-JAVA-demo-1.jar  




# application.properties参数说明
| 名称| 说明|
|----|----|
|   uri.host      | websocket地址|
|   uri.protocol  | 协议|
| uri.market.path |  websocket路径|
|    uri.port     |  端口号|


# 相关说明
项目中包含，市场行情(K线、深度、成交、Ticker)的websocket连接demo

