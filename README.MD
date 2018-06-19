### simple-rpc
---
#### 模块介绍
##### simple-rpc-remoting
    负责网络传输的模块,基于Netty4来支持网络传输,protostuff作为传输序列化工具.

##### simple-rpc-common
    存放公用的工具类,Entity,自定义异常,枚举值等.

#### 设计简介
1. 传输消息格式
    
    | 头标志位 | 消息对象类型 | 请求code | 请求流水号 | 消息长度 | 消息体 |
    | --- | ---:| ---:| ---: | ---: | ---: |
    |  2 | 1 | 1 | 8 | 4 | length | 
2. 基于 Spring