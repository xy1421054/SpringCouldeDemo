package xy.producer.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import xy.producer.pojo.Producer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author xy
 * @date 2019/8/20 13:18
 */
@RestController
public class ProducerController {

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/producer")
    @HystrixCommand(fallbackMethod = "defaultStores")
    public Producer getProduct() {

        return new Producer(Integer.valueOf(String.valueOf(null)), new Date().getTime(), "生产了111个馒头");
    }


    @GetMapping("/info")
    public String serviceUrl() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("PRODUCER", false);
        return instance.getHomePageUrl();
    }

    @GetMapping("/client")
    public String serviceUrlClient() {
        List<ServiceInstance> list = discoveryClient.getInstances("PRODUCER");
        if (list != null && list.size() > 0) {
            return String.valueOf(list.get(0).getUri());
        }
        return null;
    }

    @RequestMapping(value = "/postValue",method = RequestMethod.POST)
    public String post(@RequestBody Producer json){
        return json.toString();
    }

    public Producer defaultStores() {
        return new Producer(10, new Date().getTime(), "生产了1个馒头-熔断");
    }
}
