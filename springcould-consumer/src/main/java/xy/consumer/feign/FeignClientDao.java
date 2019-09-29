package xy.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xy.consumer.pojo.Producer;

/**
 * @author xy
 * @date 2019/9/2 17:27
 */
//@FeignClient("PRODUCER")
public interface FeignClientDao {

    @RequestMapping(method = RequestMethod.GET, value = "/producer")
    String user();

    @RequestMapping(method = RequestMethod.POST, value = "/postValue", consumes = "application/json")
    String postTest(Producer producer);
}
