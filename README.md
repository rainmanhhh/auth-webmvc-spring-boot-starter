# auth-spring-boot-starter

#### 介绍
spring cloud权限starter
- 所有纳入权限管理的微服务都应引入此starter作为依赖
- 基于com.github.rainmanhhh:ez-jwt-spring-boot-starter，以便捷支持jwt
- 可配置公共密钥service-api-key（SAK），使同集群微服务互访时鉴权直接通过，不返回403（需要鉴权服务，即网关配合）
- ServletFilter自动拦截web请求，解析http头中的jwt用户信息，并保存到UserHolder（基于threadlocal，故在异步任务中不可用）
- RequestInterceptor自动拦截向集群中的其他微服务发出的feign请求，附加SAK和jwt token

#### 软件架构
spring cloud


#### 安装教程

先clone项目并在本地运行maven install，再引入到其他项目中
