package xy.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author xy
 * @date 2019/8/19 16:35
 */
@SpringBootApplication
@EnableEurekaServer
public class ApplicationEureka {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationEureka.class,args);
    }
}
