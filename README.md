## SpringCould微服务架构

#@ 第一节阶段 Eureka 服务中心注册生产者和消费者
# Eureka 服务中心
1、端口设定：10000~10999
2、application.yml文件中设置应用相关设置
3、instance-id: ${eureka.instance.ip-address}:${server.port}@project.version@ 自定义IP地址+版本号
   ip-address: 127.0.0.1
   prefer-ip-address: true
   CONSUMER	n/a (1)	(1)	UP (1) - 127.0.0.1:120001.0-SNAPSHOT
   PRODUCER	n/a (1)	(1)	UP (1) - 127.0.0.1:11000
4、${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}}}@project.version@ 
   自定义主机名+应用名+版本号
5、开启安全认证与账号设置
    security:
      user:
        name: user
        password: 123
    basic:
      enabled: true
# Producer 生产者
1、端口设定：11000~11999
2、创建Controller，使用Producer对象来与其他model的交互，根据业务需求，采集数据源，存入数据库等等操作
# Consumer 消费者
1、端口设定：12000~12999
2、使用RestTemplate请求生产者的服务，通过EurekaClient来获取生产者的动态ip
      private RestTemplate restTemplate;
      private EurekaClient eurekaClient;
      
#@ 第二节阶段 Ribbon、Hystrix和Feign的使用
# Eureka 服务中心
1、此配置建议只试用开发和测试环境
2、心跳间隔时间,默认是30秒
   (1)org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean.leaseRenewalIntervalInSeconds
       eureka.instance.leaseRenewalIntervalInSeconds=2(客户端)
3、最后一次心跳时间后leaseExpirationDurationInSeconds秒就认为是下线了，默认是90秒
   (1)org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean.leaseExpirationDurationInSeconds
        eureka.instance.leaseExpirationDurationInSeconds=6(客户端)
4、驱逐下线的服务，间隔,5秒，默认是60
   (1)org.springframework.cloud.netflix.eureka.server.EurekaServerConfigBean.evictionIntervalTimerInMs
        eureka.server.evictionIntervalTimerInMs=5000
# Producer 生产者
1、resource文件下创建三个yml，通过配置文件的形式，启动多个Producer

2、@HystrixCommand(fallbackMethod = "defaultStores") 熔断机制
注:fallbackMethod定义的方法的参数名和返回值一定要和原参数一致

3、Hystrix的健康监测
  (1)management:
     endpoints:
       web:
         exposure:
           include: '*'   #允许显示所有信息，包括/actuator/health，/actuator/hystrix.stream
  (2)management:
        endpoints:
          web:
            exposure:
              include: ["hystrix-stream"]   #允许显示/actuator/hystrix.stream信息
  (3)management:
       endpoint:
           health:    
             show-details: always   #允许显示/actuator/health信息
# Consumer 消费者
1、@Configuration
   @RibbonClient(name = "custom", configuration = CustomConfiguration.class)
   public class TestConfiguration {
   }
创建配置文件通过Configuration注入容器，configuration可以自定义ribbon负载均衡的规则
通过ConsumerController上的getConsumer方法和ribbonTest方法测试ribbon负载均衡的算法默认-轮询
http://127.0.0.1:11001/
http://127.0.0.1:11000/
http://127.0.0.1:11001/
http://127.0.0.1:11000/
http://127.0.0.1:11001/
http://127.0.0.1:11000/
http://127.0.0.1:11001/
http://127.0.0.1:11000/
http://127.0.0.1:11001/
http://127.0.0.1:11000/
http://127.0.0.1:11001/
http://127.0.0.1:11000/
2、CustomConfiguration.class自定义负载均衡算法-随机
http://127.0.0.1:11001
http://127.0.0.1:11000
http://127.0.0.1:11000
http://127.0.0.1:11000
http://127.0.0.1:11000
http://127.0.0.1:11000
http://127.0.0.1:11000
http://127.0.0.1:11001
http://127.0.0.1:11001
3、也可以在application.yml配置文件上配置负载均衡算法
 (1)预加载配置,默认为懒加载
ribbon:
  eager-load:
    enabled: true
    clients: producer-default,producer-ribbon
 (2)这里使用服务提供者的instanceName
producer-default:
  ribbon:
    # 代表Ribbon使用的负载均衡策略
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
    # 每台服务器最多重试次数，但是首次调用不包括在内， Max number of retries on the same server (excluding the first try)
    MaxAutoRetries: 1
    # 最多重试多少台服务器，Max number of next servers to retry (excluding the first server)
    MaxAutoRetriesNextServer: 1
    # 无论是请求超时或者socket read timeout都进行重试，Whether all operations can be retried for this client
    OkToRetryOnAllOperations: true
    # Interval to refresh the server list from the source
    ServerListRefreshInterval: 2000
    # Connect timeout used by Apache HttpClient
    ConnectTimeout: 3000
    # Read timeout used by Apache HttpClient
    ReadTimeout: 3000
producer-ribbon:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.ZoneAvoidanceRule

2、feign三种用法
   (1)@FeignClient("PRODUCER") 
    普通
   (2)@FeignClient(name = "${feign.name}",url = "${feign.url}")
    url
   (3)@FeignClient( value = "PRODUCER",configuration = FooConfiguration.class)
    自定义(https://cloud.spring.io/spring-cloud-static/Greenwich.SR2/single/spring-cloud.html#spring-cloud-feign)
    按照官网上配置即可
logging:
  level:
   xy.consumer.feign.FeignClientFooConfigurationDao: info #配置feign日志
   
3、Feign Hystrix Fallbacks 嵌套(官网案例)
.yml文件中配置
  feign:
    hystrix:
       enabled: true #开启feign 熔断 否则无效
(1)@FeignClient(name = "hello", fallback = HystrixClientFallback.class)
   protected interface HystrixClient {
       @RequestMapping(method = RequestMethod.GET, value = "/hello")
       Hello iFailSometimes();
   }
   static class HystrixClientFallback implements HystrixClient {
       @Override
       public Hello iFailSometimes() {
           return new Hello("fallback");
       }
   }
(2)@FeignClient(name = "hello", fallbackFactory = HystrixClientFallbackFactory.class)
   protected interface HystrixClient {
   	@RequestMapping(method = RequestMethod.GET, value = "/hello")
   	Hello iFailSometimes();
   }
   @Component
   static class HystrixClientFallbackFactory implements FallbackFactory<HystrixClient> {
   	@Override
   	public HystrixClient create(Throwable cause) {
   		return new HystrixClient() {
   			@Override
   			public Hello iFailSometimes() {
   				return new Hello("fallback; reason was: " + cause.getMessage());
   			}
   		};
   	}
   }
   
4、Hystrix Dashboard /hystrix.stream 详细信息的图形化
   按照官网的说明即可
  (1)To include the Hystrix Dashboard in your project, use the starter with a group ID of org.springframework.cloud and an 
  artifact ID of spring-cloud-starter-netflix-hystrix-dashboard. 
  See the Spring Cloud Project page for details on setting up your build system with the current Spring Cloud Release Train.
  (2)To run the Hystrix Dashboard, annotate your Spring Boot main class with @EnableHystrixDashboard. 
  Then visit /hystrix and point the dashboard to an individual instance’s /hystrix.stream endpoint in a Hystrix client application.
  (3)输入:http://127.0.0.1:12000/hystrix
     在Hystrix-DashBoard界面输入要监控的地址，
     地址格式：http://host + port + /actuator/hystrix.stream，
     例如：http://127.0.0.1:12000/actuator/hystrix.streamm，实现对该地址的监控
     

#@ 第三节阶段 Turbine和Zuul使用
# Eureka 服务中心

# Producer 生产者

# Consumer 消费者
 1、yml文件设置 turbine @EnableTurbine(开启turbine) 
   turbine:
     aggregator:
       clusterConfig: CONSUMER
     appConfig: consumer
 http://127.0.0.1:12000/actuator/hystrix.streamm 地址
 输入：http://127.0.0.1:12000/turbine.stream?cluster=CONSUMER (多消费端，测试效果更佳)
 
 2、turbine监控多个应用
   turbine:
     aggregator:
       clusterConfig: default
     appConfig: consumer,producer,consumer-test(三个应用) 
     cluster-name-expression: "'default'"
  http://127.0.0.1:12000/actuator/hystrix.streamm 地址
  输入：http://127.0.0.1:12000/turbine.stream
 
 3、解决路径匹配的两种方法
  (1)turbine.instanceUrlSuffix+应用的name,用于不同应用server.servlet.context-path: 多加路径匹配
  (2)eureka:
       instance:
         metadata-map:
           management.port: ${management.port:8081}
   使用management.port管理
   详情可以查看官方https://cloud.spring.io/spring-cloud-static/Greenwich.SR2/single/spring-cloud.html#_hystrix_metrics_stream
   
# Zuul 网关
1、端口设定：9999
2、Zuul的使用要导入eureka-client依赖+zuul依赖，自带熔断
3、Zuul 路由配置映射的几种方式
    (1)自定义服务映射地址 
    (2)Path方式映射
    (3)Url方式映射
    (4)表达式方式映射
    (5)前缀表达方式
    (6)其他配置
4、Zuul 上传文件
5、Zuul fallback
6、SideCar 访问异构服务
7、Zuul 过滤器
8、Zuul 高可用
# Spring Cloud Config Service
1、主要是管理.yml文件有 config server、config client使用
2、config通配符模式、config模式匹配 、config搜索路径和密码、config加密、config加密、configServer认证
3、Config整合Eureka、手动刷新Config配置、自动刷新Config

## 总结
总体上spring could功能确实不错，底层还没有细细的研究。需要配置可能会多些，从后面的Zuul网关
和SpringCloudConfigService内容就一笔带过，可以在spring官网上根据文档说明自己体验下。实际应用中
在配合一些数据库存储，rpc调用，Elasticsearch搜索引擎也好等等一些功能，就能事半功倍了。

