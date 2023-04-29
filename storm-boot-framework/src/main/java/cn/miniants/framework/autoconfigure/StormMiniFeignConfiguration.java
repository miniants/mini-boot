package cn.miniants.framework.autoconfigure;

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
public class StormMiniFeignConfiguration {
    @Bean
    @Primary
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new CustomFeignDecoder(messageConverters);
    }
}
