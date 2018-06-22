### simple-rpc
---
#### 模块介绍
##### simple-rpc-common
    存放公用的工具类,Entity,自定义异常,枚举值等.
##### simple-rpc-config
    负责配置管理，获取配置信息的值等操作
##### simple-rpc-core
    RPC 框架核心模块，负责 Provider 服务端的实现和 Client 客户端的实现
    - protocol
        负责传输编码与解码
    - provider
        负责服务端核心实现
    - client
        负责客户端核心实现，动态代理，消息传输与解析

#### 设计简介
1. 传输消息格式
    
        消息长度（4位）+ 序列化消息
     
2. Spring 支持

3. 基于 Netty 传输

4. 基于 ProtoBuf 序列化

#### TODO LIST
1. 规范化日志输出
2. 服务注册与发现
3. 优雅停机
4. 本地负载均衡
5. 服务管理与调用链查询
6. 监控 熔断机制