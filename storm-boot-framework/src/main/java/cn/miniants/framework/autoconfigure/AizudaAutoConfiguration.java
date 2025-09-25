
package cn.miniants.framework.autoconfigure;

import cn.miniants.framework.mapper.log.IOplogStorageProvider;
import cn.miniants.framework.mapper.log.OplogAspect;
import cn.miniants.framework.advice.MiniControllerResultAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;

@Lazy
@Configuration(proxyBeanMethods = false)
@Import({WebMvcConfig.class,
        JsonAutoConfiguration.class, MybatisPlusConfiguration.class, RedisCacheAutoConfiguration.class,
        RedisLockAutoConfiguration.class, AuthConfiguration.class, MiniFeignConfiguration.class})
@ComponentScan(basePackages = {"cn.miniants.framework.security"})
public class AizudaAutoConfiguration {

    /**
     * 全局异常处理
     */
    @Bean
    @Order
    @ConditionalOnMissingBean
    public MiniControllerResultAdvice serviceExceptionHandler() {
        return new MiniControllerResultAdvice();
    }

//    @Bean
//    @ConditionalOnClass(SSOAuthorization.class)
//    @ConditionalOnProperty(prefix = "kisso.config", name = "enabled", havingValue = "true", matchIfMissing = true)
//    @ConditionalOnMissingBean
//    public ServiceWebMvcConfigurer serviceWebMvcConfigurer(@Autowired(required = false) SSOAuthorization ssoAuthorization,
//                                                           @Autowired(required = false) IExcludePaths excludePaths) {
//        Assert.notNull(ssoAuthorization, "SSOAuthorization Implementation class not found");
//        return new ServiceWebMvcConfigurer(ssoAuthorization, excludePaths);
//    }
//
//    /**
//     * spring boot admin 访问监控权限拦截器配置
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(prefix = "spring.boot.admin.client.instance.metadata", name = "user.name")
//    public FilterRegistrationBean basicAuthenticateFilter(@Value("${spring.boot.admin.client.instance.metadata.user.name}") String username,
//                                                          @Value("${spring.boot.admin.client.instance.metadata.user.password}") String password) {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new BasicAuthenticateFilter(username, password));
//        registrationBean.addUrlPatterns("/actuator", "/actuator/**");
//        registrationBean.setOrder(Integer.MAX_VALUE);
//        return registrationBean;
//    }

    /**
     * 操作日志拦截切面
     */
    @Bean
    @ConditionalOnBean(IOplogStorageProvider.class)
    public OplogAspect logRecordAspect(IOplogStorageProvider oplogStorageProvider) {
        return new OplogAspect(oplogStorageProvider);
    }
}
