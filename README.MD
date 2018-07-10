### Simple-RPC
    Simple-RPC是基于 Thrift 实现的 Java RPC 框架。
    在个人平时学习的过程总，希望加深对 Thrift 和 RPC 的理解，故开发了一个简单的 RPC 框架。
    Simple-RPC对 Thrift 客户端的改造主要从如下：
        1. 服务发现基于 Zookeeper 自动发现，由于 Zookeeper 原生客户端使用较为复杂，因此使用curator-recipes实现
        2. 客户端使用连接池对服务调用进行管理，提升性能。使用 apache commons pool2 进行连接池开发，降低代码复杂度
        3. LoadBalance：当 Zookeeper 节点的 Watcher 监听到节点变更后，通知客户端重新刷新服务器节点信息。
            而负载均衡有很多算法，这里使用了简单的加权轮询的算法
        4. 客户端与 Spring 解耦，通过 simple-rpc-spring 工程，提供 Spring 支持
        5. 客户端自定义支持 Filter，可以在调用远程方法前和方法调用后执行自定义过滤器逻辑
        6. 支持静态路由，指定服务和目标机器信息
    其他特性：
        1. 添加 Trace 追踪支持：
            通过重写 TProtocolDecorator 自定义消息接收和传输的过程，从消息中获取每次请求的 trace 信息。 
            同时，Trace 信息通过 ThreadLocal 写入到当前线程上下文，可以支持服务调用链追踪
            通过SLF4J 的 MDC，存储 traceId 对象，完成日志的 trace 追踪
        2. 上下文 Context：
            ClientContext 对象存放当前线程调用的服务信息，如：服务名称 参数 目标机器信息等。存储在 ThreadLocal 中
            ProviderContext 对象存放当前线程的调用方信息，如：客户端机器信息等
    
---
#### 模块介绍
##### simple-rpc-basic
    存放核心的基本类，工具类等信息
    - entity 自定义实体信息
    - exception 自定义异常
    - utils 工具类 
#### simple-rpc-client
    RPC 框架客户端实现，负责创建调用方客户端生成，过滤器等
    - filter 客户端过滤器
    - pool 客户端连接池工厂
    - proxy 服务调用动态代理实现
##### simple-rpc-core
    RPC 框架核心模块
    - loadbalance 负载均衡实现
    - protocol 通信协议重写
    - trace 追踪 trace 支持
    - zookeeper zk服务注册与发现 
##### simple-rpc-spring
    - client 调用方 Spring 支持
##### simple-rpc-config
    - 配置管理
##### simple-rpc-test
    单元测试

#### 使用说明
1. 调用方
    
2. 服务提供方

#### TODO LIST
1. 规范化日志输出
2. 优雅停机
3. 监控 熔断机制
4. 服务版本支持
5. 异步调用
6. Codegen 代码生成工具
7. 本地 mock
