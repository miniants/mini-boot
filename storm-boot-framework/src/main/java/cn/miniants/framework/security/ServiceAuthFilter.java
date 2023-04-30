package cn.miniants.framework.security;

import cn.hutool.core.util.URLUtil;
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

@ConditionalOnProperty(prefix = "miniants.auth", name = "enabled", havingValue = "true")
@Component
public class ServiceAuthFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //认证信息从Header 或 请求参数 中获取
        String jwt_credentials = request.getHeader(JWT_CREDENTIALS);

        if (null != jwt_credentials) {
            String jwt_credentials_json = URLUtil.decode(jwt_credentials);
            JsonNode jwtObject = JSONUtil.readTree(jwt_credentials_json);
            ThreadLocalUtils.put(JWT_USER_SESSION, UserSession.builder()
                    .username(null==jwtObject.get("username")?null:jwtObject.get("username").asText())
                    .authorities(null==jwtObject.get("authorities")?null:jwtObject.get("authorities").asText())
                    .id(null==jwtObject.get("userId")?null:jwtObject.get("userId").asLong())
                    .clientId(null==jwtObject.get("clientId")?null:jwtObject.get("clientId").asText())
                    .build());

        }

        //TODO服务间的认证再这里做，目前先都放行
        return true;
    }
}
