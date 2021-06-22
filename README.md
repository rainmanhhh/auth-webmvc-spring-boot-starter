# auth-webmvc-spring-boot-starter

#### 介绍
spring cloud权限starter(webmvc)
- 所有纳入权限管理的webmvc微服务都应引入此starter作为依赖
- 基于com.github.rainmanhhh:ez-jwt-spring-boot-starter，以便捷支持jwt
- 基于com.github.rainmanhhh:auth-sak-spring-boot-starter，以便捷支持SAK(service-api-key)
- ServletFilter自动拦截web请求，解析http头中的jwt用户信息，并保存到UserHolder（基于threadlocal，故在异步任务中不可用）
- RequestInterceptor自动拦截向集群中的其他微服务发出的feign请求，附加SAK和jwt token

#### 软件架构
spring cloud


#### 安装教程

添加jitpack到repositories中，再用maven引入
