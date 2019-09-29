package xy.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author xy
 * @date 2019/9/20 10:03
 */
@SpringBootApplication
@EnableZuulProxy
public class ApplicationZuul {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationZuul.class,args);
    }
}
