package cn.miniants.framework.interceptor;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cn.miniants.framework.constant.StormwindConstant.GatewayConstants.JWT_CREDENTIALS_HEADER;
import static cn.miniants.framework.constant.StormwindConstant.GatewayConstants.SERVICE_INSTANCE_HOST;

public class MiniFeignRequestInterceptor implements RequestInterceptor {
    @Resource
    private NacosDiscoveryClient nacosDiscoveryClient;

    @Value("${miniants.feign.throw-on-host-miss:false}")
    private boolean throwOnHostMiss = false;

    //    private final String serviceId;
//
//    public MiniFeignRequestInterceptor(String serviceId) {
//        this.serviceId = serviceId;
//    }
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 获取 token HTTP头
            String token = request.getHeader(JWT_CREDENTIALS_HEADER);
            if (token != null) {
                requestTemplate.header(JWT_CREDENTIALS_HEADER, token);
            }


            // 获取 instance-host HTTP头
            String instanceHost = request.getHeader(SERVICE_INSTANCE_HOST);
            // 如果存在，将 instance-host 添加到 Feign 请求头中
            if (instanceHost != null) {
                requestTemplate.header(SERVICE_INSTANCE_HOST, instanceHost);

                String serviceName = requestTemplate.feignTarget().name();
                List<ServiceInstance> instances = nacosDiscoveryClient.getInstances(serviceName);
                ServiceInstance selectedInstance = instances.stream()
                        .filter(instance -> instanceHost.equals(instance.getHost()))
                        .findFirst()
                        .orElse(null);

                // 如果找到匹配的实例，则将其设置为 Feign 客户端的请求 URL
                if (selectedInstance != null) {
                    String scheme = selectedInstance.getScheme() != null ? selectedInstance.getScheme() : "http";
                    String selectHostUrl = scheme + "://" + selectedInstance.getHost() + ":" + selectedInstance.getPort() + "/";
                    String selectServiceUrl = requestTemplate.feignTarget().url().replaceAll(scheme + "://.*/", selectHostUrl);
                    requestTemplate.target(selectServiceUrl);
                } else if(throwOnHostMiss){
                    throw new RuntimeException("FeignClient未找到目标实例%s来调用服务%s".formatted(instanceHost, serviceName));
                }
            }

        }

    }
}
