package cn.miniants.framework.interceptor;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static cn.miniants.framework.constant.StormwindConstant.GatewayConstants.SERVICE_INSTANCE_HOST;
import static cn.miniants.framework.constant.StormwindConstant.GatewayConstants.SERVICE_INSTANCE_ID;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Getter
@Setter
@Slf4j
public class MiniLoadBalancerFilter extends ReactiveLoadBalancerClientFilter {

    @Resource
    private DiscoveryClient discoveryClient;

    @Value("${miniants.gateway.throw-on-host-miss:false}")
    private boolean throwOnHostMiss = false;

    public MiniLoadBalancerFilter(LoadBalancerClientFactory clientFactory, GatewayLoadBalancerProperties properties) {
        super(clientFactory, properties);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        // 获取需要的实例ID和实例主机信息
        String instanceId = serverHttpRequest.getHeaders().getFirst(SERVICE_INSTANCE_ID);
        if (Objects.isNull(instanceId)) {
            instanceId = serverHttpRequest.getQueryParams().getFirst(SERVICE_INSTANCE_ID);
        }
        String instanceHost = serverHttpRequest.getHeaders().getFirst(SERVICE_INSTANCE_HOST);
        if (Objects.isNull(instanceHost)) {
            instanceHost = serverHttpRequest.getQueryParams().getFirst(SERVICE_INSTANCE_HOST);
        }

        // 如果没有指定实例ID和实例主机，使用默认的过滤器逻辑
        if (StrUtil.isBlank(instanceId) && StrUtil.isBlank(instanceHost)) {
            return super.filter(exchange, chain);
        }

        // 获取原始路由信息和服务名称
        Route originalRoute = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        String serviceName = originalRoute.getUri().getHost();
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

        // 查找与实例ID或实例主机匹配的目标服务实例
        String finalInstanceId = instanceId;
        String finalInstanceHost = instanceHost;
        ServiceInstance targetInstance = instances.stream()
                .filter(instance -> instance.getInstanceId().equals(finalInstanceId) || instance.getHost().equals(finalInstanceHost))
                .findFirst()
                .orElse(null);

        // 如果找到目标实例，则使用自定义选择的实例
        if (targetInstance != null) {
            URI requestUrl = super.reconstructURI(targetInstance, serverHttpRequest.getURI());
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);

            DefaultResponse response = new DefaultResponse(targetInstance);
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_LOADBALANCER_RESPONSE_ATTR, response);

            return chain.filter(exchange);
        } else {
            if (throwOnHostMiss) {
                throw new RuntimeException("Gateway未找到目标实例%s来调用服务%s。".formatted(finalInstanceHost, serviceName));
            } else {
                // 如果未找到目标实例，使用默认的过滤器逻辑
                return super.filter(exchange, chain);
            }
        }
    }
}
