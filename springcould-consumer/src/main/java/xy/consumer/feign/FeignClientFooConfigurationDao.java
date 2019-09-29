package xy.consumer.feign;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xy.consumer.config.FooConfiguration;
import xy.consumer.config.HystrixClientFallback;
import xy.consumer.pojo.Producer;

/**
 * @author xy
 * @date 2019/9/2 17:27
 */
@FeignClient( value = "PRODUCER",configuration = FooConfiguration.class,fallback =HystrixClientFallback.class )
public interface FeignClientFooConfigurationDao {

//    @RequestMapping(method = RequestMethod.GET, value = "/producer")
    @RequestLine("GET /producer")
    String user();

//    @RequestMapping(method = RequestMethod.POST, value = "/postValue", consumes = "application/json")
    @RequestLine("POST /postValue")
    String postTest(Producer producer);


}
