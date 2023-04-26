package cn.miniants.framework.autoconfigure;

import cn.miniants.framework.security.ServiceAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    @Value("${miniants.auth.enabled:false}")
    private boolean serviceAuthFilterEnabled;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (serviceAuthFilterEnabled) {
            registry.addInterceptor(new ServiceAuthFilter()).addPathPatterns("/**");
        }
    }
}
