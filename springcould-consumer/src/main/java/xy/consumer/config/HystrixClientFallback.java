package xy.consumer.config;

import org.springframework.stereotype.Component;
import xy.consumer.feign.FeignClientFooConfigurationDao;
import xy.consumer.pojo.Producer;

/**
 * @author xy
 * @date 2019/9/5 16:43
 */
@Component
public class HystrixClientFallback implements FeignClientFooConfigurationDao {
    @Override
    public String user() {
        return "fallback";
    }

    @Override
    public String postTest(Producer producer) {
        return null;
    }
}
