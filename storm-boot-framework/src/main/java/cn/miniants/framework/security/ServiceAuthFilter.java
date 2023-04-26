package cn.miniants.framework.security;

import cn.miniants.framework.web.UserSession;
import cn.miniants.toolkit.JSONUtil;
import cn.miniants.toolkit.ThreadLocalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.miniants.framework.constant.StormwindConstant.AuthConstants.JWT_CREDENTIALS;
import static cn.miniants.framework.constant.StormwindConstant.AuthConstants.JWT_USER_SESSION;

/**
 * 这个过滤器是给服务间调用的时候使用的，比如服务A调用服务B，服务B需要验证服务A的合法性
 */
@Component
//@ConditionalOnProperty(prefix = "miniants.auth",name = "enabled", havingValue = "true")
public class ServiceAuthFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //认证信息从Header 或 请求参数 中获取
        String jwt_credentials = request.getHeader(JWT_CREDENTIALS);
        JsonNode jwtObject = JSONUtil.readTree(jwt_credentials);

        ThreadLocalUtils.put(JWT_USER_SESSION, UserSession.builder()
                .username(jwtObject.get("username").asText())
                .authorities(jwtObject.get("authorities").asText())
                .id(jwtObject.get("id").asLong())
                .clientId(jwtObject.get("clientId").asText())
                .build());

//        UserSession userSession = ;
        return preHandle(request, response, handler);
    }
}
