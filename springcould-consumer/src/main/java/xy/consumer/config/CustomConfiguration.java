package xy.consumer.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * @author xy
 * @date 2019/8/21 16:47
 */
public class CustomConfiguration {

    @Autowired
    private IClientConfig clientConfig;

    /**
     * ribbon 负载均衡的算法-随机
     *
     * @param config
     * @return
     */
    @Bean
    public IRule ribbonRule(IClientConfig config) {
        RandomRule randomRule = new RandomRule();
        return randomRule;
    }

    /**
     *
     * @return
     */
   /* @Bean
    public IRule ribbonRule() {
        return new BestAvailableRule();
    }*/
}
