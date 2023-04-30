package cn.miniants.framework.autoconfigure;

import cn.miniants.framework.interceptor.MiniFeignRequestInterceptor;
import feign.Feign;
import feign.codec.Decoder;
import cn.miniants.framework.advice.MiniFeignDecoder;
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

//    @Bean
//    public RequestInterceptor customRequestInterceptor() {
//        return new CustomFeignRequestInterceptor();
//    }

    @Bean
    public Feign.Builder miniFeignBuilder() {
        return Feign.builder().requestInterceptor(template -> {
            String serviceId = template.feignTarget().type().getAnnotation(FeignClient.class).name();
            new MiniFeignRequestInterceptor(serviceId).apply(template);
        });
    }
}
