package cn.miniants.framework.autoconfigure;

import cn.miniants.framework.security.ServiceAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class AuthConfiguration implements WebMvcConfigurer {
    @Autowired(required = false)
    private ServiceAuthFilter serviceAuthFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (serviceAuthFilter != null) {
            registry.addInterceptor(serviceAuthFilter).addPathPatterns("/**");
        }
    }
}
