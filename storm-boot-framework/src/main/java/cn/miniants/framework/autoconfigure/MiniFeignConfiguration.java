package cn.miniants.framework.autoconfigure;

import cn.miniants.framework.advice.MiniFeignErrorDecoder;
import cn.miniants.framework.interceptor.MiniFeignRequestInterceptor;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import cn.miniants.framework.advice.MiniFeignDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import(FeignClientsConfiguration.class)
public class MiniFeignConfiguration {
    @Bean
    @Primary
    public Decoder myFeignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new MiniFeignDecoder(messageConverters);
    }

    /**
     * feign 响应异常异常处理
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new MiniFeignErrorDecoder();
    }

    /**
     * feign 调用拦截器,在请求头中加入服务名，指定调用哪个服务provider
     * @return
     */
    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new MiniFeignRequestInterceptor();
    }

//    @Bean
//    @Primary
//    public Feign.Builder miniFeignBuilder() {
//        return Feign.builder().requestInterceptor(template -> {
//            String serviceId = template.feignTarget().type().getAnnotation(FeignClient.class).name();
//            new MiniFeignRequestInterceptor(serviceId).apply(template);
//        });
//    }
}
