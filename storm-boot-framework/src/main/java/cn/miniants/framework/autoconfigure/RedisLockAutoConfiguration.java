package cn.miniants.framework.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 *  redis分布式锁配置
 * @author guoqianyou
 * @date 2023/4/7 19:22
 */
@Lazy
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedisOperations.class, RedisConnectionFactory.class})
public class RedisLockAutoConfiguration {
    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "stormwindLock");
    }
}
