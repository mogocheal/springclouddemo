UMeteo 气象数据服务
---

UMeteo 气象数据服务旨在融合多源异构数据，提供统一数据环境。

数据服务采用 Spring Cloud Netflix 技术栈。
Spring Cloud Netflix 通过自动化配置、Spring 环境绑定及其他 Spring 编程惯例为 Spring Boot 应用提供 Netflix OSS 集成。
通过少量 annotation 你就可以快速启用和配置应用中的常用模式，使用久经沙场的 Netflix components 构建大型分布式系统。
提供的模式包括：服务发现(Eureka)，断路器(Hystrix)，智能路由(Zuul)，以及客户端负载均衡(Ribbon)。

![image](https://www.daocloud.io/assets/images/dce-platform.png)

单体应用通常指在一个程序中满足多个业务或技术领域的需求，不同的需求领域内化为模块。
假定我们要开发一个Web应用，通常的MVC模式可以满足要求。
放在一个应用里处理所有的事情的好处是非常大的，比如程序的调试相对容易，执行效率高。

但是单体的应用在规模变大后其前述好处会很快衰减。
随着业务的增长，需求的调整和变更，应用内部会以模块为基础进行重构，增加和删减、改变模块的能力。
应用逐渐超越开发人员所能掌控的范围，代码死角开始出现，重构变得困难。
针对任何一个模块的扩容或升级都是对整个应用的所有模块的扩容和升级。
程序对外界的依赖越来越复杂，自动化测试的覆盖率低。

微服务的概念就是每个服务只处理一件事情，应用由多个服务构成。
服务之间通过Web协议进行通信，例如http/json。
代码要易于抛弃：出问题的代码可以更容易地重写。
运行的服务也要易于抛弃，升级时用一个新的服务实例替代旧的实例。

把单体应用分解为一些列微服务，开发团队也可以进行“重构”。
每个小团队负责一个服务，维护和学习成本下降了。
由于服务间通过接口进行连接，
每个服务的内部实现机制可以根据领域选择更合适的技术，混合编程是很自然的事情，
试错的成本降低、试错的频率加快，从而意味着创新的速度可以提高。


说明
---

项目分为三个部分，三个部分各自可以在运行时根据负载水平扩展或收缩。

* Zuul 网关
* Eureka 服务发现
* 服务（n个）

![image](https://camo.githubusercontent.com/5e596c573110bffb608614a09c97611107205d0d/687474703a2f2f6e6574666c69782e6769746875622e696f2f7a75756c2f696d616765732f7a75756c2d706879736963616c2d617263682e706e67)

Netflix 公司是这样使用它的。

![image](https://camo.githubusercontent.com/4eb7754152028cdebd5c09d1c6f5acc7683f0094/687474703a2f2f6e6574666c69782e6769746875622e696f2f7a75756c2f696d616765732f7a75756c2d726571756573742d6c6966656379636c652e706e67)

Zuul 可以用来实现以下功能：
- 授权与安全-为每个资源验证身份，拒绝掉不符的请求
- 监视-跟踪有意义的数据，edge统计数据，以便对产品有精确的见解
- 动态路由-按需动态路由请求到不同的后端集群
- 压力测试-逐渐增加到集群的流量以便衡量性能
- 减载-为不同请求分配处理能力，丢弃超过限度的请求
- 静态响应处理-直接在edge构建响应而不是转发到内部集群
- 多区域弹性-夸多个AWS区域路由请求，以便分散ELB的使用，让edge更靠近用户（内网环境用不到）

![image](https://github.com/Netflix/eureka/raw/master/images/eureka_architecture.png)

Eureka 由两个组件组成：
Eureka 服务器 和 Eureka 客户端 。
Eureka 服务器用作服务注册服务器。
Eureka 客户端是一个 java 客户端，
用来简化与服务器的交互、作为轮询负载均衡器，
并提供服务的故障切换支持。
Netflix 在其生产环境中使用的是另外的客户端，
它提供基于流量、资源利用率以及出错状态的加权负载均衡。
当一个中间层服务首次启动时，他会将自己注册到 Eureka 中，
以便让客户端找到它，同时每 30 秒发送一次心跳。
如果一个服务在几分钟内没有发送心跳，
它将从所有 Eureka 节点上注销。

一个 Amazon 域中可以有一个 Eureka 节点集群，
每个可用区（Availability Zone）至少有一个 Eureka 节点。
AWS 的域相互之间是隔离的。

对等感知部署参见： http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html#_peer_awareness


启动
---

一个局域网只用启动一个 Eureka 服务发现服务和 Zuul 网关，启动方式：

```
# eureka
mvn spring-boot:run
# zuul
mvn spring-boot:run
```

服务启动方式：

```
# 某个服务，比如 phoenix 服务
mvn spring-boot:run
```

hystrix dashboard 启动：

```
# hystrix
mvn spring-boot:run
```

文档项目
---

TODO 文档工程可以独立于本项目开发部署，考虑使用 [aglio](https://www.npmjs.com/package/aglio) 之类的工具生成，
简单点可以直接写 markdown，转 html 静态页面。
网上也有swagger之类的工具号称能自动生成文档，也可以纳入考虑范围。

已经在 Zuul 配置中预留了文档路径：http://localhost:8000/api

文档工程部署好以后在 Zuul 配置中直接绑定 url 即可。

开发
---

目前项目包括的模块应该覆盖了大部分气象数据源，如果有新的数据源需要提供统一接口，可以添加新模块。

不需要特别配置，新添加的模块启动后，新服务会自动被 eureka 发现，由 zuul 网关控制。

使用
---

Zuul 访问应用服务接口：
- http://localhost:8000/api/{服务名}/ + 服务提供的访问路径
- http://localhost:8000/api/cimiss/ + cimiss 服务提供的访问路径

Eureka 仪表盘：http://localhost:8761/

Eureka 注册的服务：http://localhost:8761/eureka/apps

Eureka 环境：http://localhost:8761/env

接口文档：http://localhost:8000/api

Hystrix Dashboard 监控请求状态：http://localhost:8080/hystrix ，
在界面输入 zuul 的监控地址：http://localhost:8001/admin/hystrix.stream 。
如果觉得输入每个 zuul 的监控地址很low的话也可以再配置个Turbine，看是否需要在配置。

其他
---

可以使用 spring cloud config 实现远程配置，远程配置各个项目的属性，后面看是否需要再作配置。

Docker 部署
---------

用法及好处参考

https://www.docker.com/use-cases

