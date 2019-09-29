package xy.consumer.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author xy
 * @date 2019/8/21 16:46
 */
@Configuration
@RibbonClient(name = "PRODUCER", configuration = CustomConfiguration.class)
//@RibbonClient(name = "PRODUCER")
public class RibbonConfig {
}
