package cn.miniants.framework.autoconfigure;

import cn.miniants.framework.interceptor.CustomFeignRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import cn.miniants.framework.advice.CustomFeignDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
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
        return new CustomFeignDecoder(messageConverters);
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return new CustomFeignRequestInterceptor();
    }
}
