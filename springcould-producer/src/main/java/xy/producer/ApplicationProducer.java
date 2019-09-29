package xy.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * @author xy
 * @date 2019/8/19 11:02
 */
@SpringBootApplication
@EnableCircuitBreaker
public class ApplicationProducer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationProducer.class,args);
    }
}
