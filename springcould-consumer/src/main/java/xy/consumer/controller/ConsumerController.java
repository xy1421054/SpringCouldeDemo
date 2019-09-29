package xy.consumer.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import xy.consumer.feign.FeignClientDao;
import xy.consumer.feign.FeignClientFooConfigurationDao;
import xy.consumer.feign.FeignClientUrlDao;
import xy.consumer.pojo.Producer;

import javax.annotation.Resource;
import java.net.URI;

/**
 * @author xy
 * @date 2019/8/19 16:49
 */
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

//    @Resource
//    private FeignClientDao feignClientDao;
    @Resource
    private FeignClientFooConfigurationDao feignClientDao;

    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    @GetMapping("/getInfo")
    public String getConsumer(){
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("PRODUCER", false);
        String homePageUrl = instance.getHomePageUrl();
        System.out.println(homePageUrl);
        String forObject = restTemplate.getForObject(homePageUrl+"/producer", String.class);
        return forObject;
    }

    /**
     * 任意选择一个输出做测试
     */
    @GetMapping("/ribbonTest")
    public void ribbonTest(){
        ServiceInstance instance = loadBalancerClient.choose("PRODUCER");
        URI storesUri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
        System.out.println(storesUri);
        // ... do something with the URI
//        System.out.println(""+instance.getServiceId()+instance.getHost()+ instance.getInstanceId());
    }

    @GetMapping("/feign")
    public void feignTest(){
        String user = feignClientDao.user();
        System.out.println(user);
    }

    @RequestMapping(value = "/feignPost",method = RequestMethod.POST,consumes = "application/json")
    public String feignPostTest(@RequestBody Producer producer){
        String producer1 = feignClientDao.postTest(producer);
        System.out.println(producer1);
        return producer1;
    }
}
