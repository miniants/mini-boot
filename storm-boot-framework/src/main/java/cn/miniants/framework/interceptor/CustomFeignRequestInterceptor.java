package cn.miniants.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static cn.miniants.framework.constant.StormwindConstant.GatewayConstants.SERVICE_INSTANCE_HOST;

public class CustomFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // 获取 instance-host HTTP头
            String instanceHost = request.getHeader(SERVICE_INSTANCE_HOST);

            // 如果存在，将 instance-host 添加到 Feign 请求头中
            if (instanceHost != null) {
                requestTemplate.header(SERVICE_INSTANCE_HOST, instanceHost);
            }
        }

    }
}
