package xy.consumer.config;

import feign.Contract;
import feign.Feign;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author xy
 * @date 2019/9/4 13:21
 */
@Configuration
public class FooConfiguration {

    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    /**
     * feign 日志
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    /**
     * 登陆验证
     * @return
     */
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("user", "password");
//    }
}
