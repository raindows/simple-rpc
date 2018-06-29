### simple-rpc
---
#### 模块介绍
##### simple-rpc-basic
    存放核心的基本类，工具类等信息
    - entity
    - utils
##### simple-rpc-core
    RPC 框架核心模块，负责 Provider 服务端的实现和 Client 客户端的实现
    - protocol
        负责创建客户端的TProtocol对象池工厂

#### simple-rpc-client
    RPC 框架客户端实现，负责创建调用方客户端生成，过滤器等
    - client
    - filter

#### 设计简介
1. 基于 Facebook Thrift 进行开发
    Thrift 0.11.0

#### TODO LIST
1. 规范化日志输出
2. 服务注册与发现
3. 优雅停机
4. 本地负载均衡
5. 服务管理与调用链查询
6. 监控 熔断机制
7. 服务版本支持
8. 异步调用
9. Codegen 代码生成工具
10. Spring Support