
package cn.miniants.framework.web;

import cn.hutool.http.HttpStatus;
import com.baomidou.kisso.SSOAuthorization;
import com.baomidou.kisso.web.handler.SSOHandlerInterceptor;
import com.baomidou.kisso.web.interceptor.SSOPermissionInterceptor;
import com.baomidou.kisso.web.interceptor.SSOSpringInterceptor;
import cn.miniants.framework.interceptor.ControllerStatusHandlerInterceptor;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**

 * ----------------------------------------
 * Service Web 相关配置
 *
 * @author 青苗
 * @since 2022-02-27
 */
@Slf4j
@AllArgsConstructor
public class ServiceWebMvcConfigurer implements WebMvcConfigurer {
    private SSOAuthorization ssoAuthorization;
    private IExcludePaths excludePaths;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // SSO 授权拦截器
        SSOSpringInterceptor ssoInterceptor = new SSOSpringInterceptor();
        ssoInterceptor.setHandlerInterceptor(new SSOHandlerInterceptor() {
            private static final String CONTENT_TYPE_ERR = "application/json; charset=utf-8";

            @Override
            public boolean preTokenIsNullAjax(HttpServletRequest request, HttpServletResponse response) {
                return false;
            }

            @SneakyThrows
            @Override
            public boolean preTokenIsNull(HttpServletRequest request, HttpServletResponse response) {
                log.info("没有登录需要登录后才能访问！ 访问路径={}", request.getRequestURI());
                response.setCharacterEncoding("UTF-8");
                response.setContentType(CONTENT_TYPE_ERR);
                response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
                response.getWriter().write("没有登录，禁止访问");
                return false;
            }
        });
        InterceptorRegistration registration = registry.addInterceptor(ssoInterceptor);
        registration.addPathPatterns("/**");

        // 权限拦截器
        SSOPermissionInterceptor permissionInterceptor = new SSOPermissionInterceptor();
        permissionInterceptor.setAuthorization(this.ssoAuthorization);

        //controller状态拦截器
        registry.addInterceptor(new ControllerStatusHandlerInterceptor());

        // 没有注解的情况下 直接跳过， 默认任何权限都可以访问
        permissionInterceptor.setNothingAnnotationPass(true);
        InterceptorRegistration registrationPermission = registry.addInterceptor(permissionInterceptor);
        registrationPermission.addPathPatterns("/**");

        // 排除登录权限验证
        List<String> eps = new ArrayList<>();
        eps.add("/v3/api-docs/**");
        if (null != excludePaths) {
            eps.addAll(excludePaths.getPaths());
        }
        registration.excludePathPatterns(eps);
        registrationPermission.excludePathPatterns(eps);
    }
}
