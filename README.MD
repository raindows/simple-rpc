### simple-rpc
---
#### 模块介绍
##### simple-rpc-common
    存放公用的工具类,Entity,自定义异常,枚举值等.
##### simple-rpc-config
    存放与框架配置有关信息，包括服务器位置配置，注册中心配置，负载均衡配置
##### simple-rpc-core
    RPC 框架核心模块，负责 Provider 服务端的实现和 Client 客户端的实现
##### simple-rpc-serialization
    RPC 框架序列化模块

#### 设计简介
1. 传输消息格式
    
        消息长度（4位）+ 序列化消息
     
2. Spring 支持

3. 基于 Netty 传输

4. 基于 ProtoBuf 序列化