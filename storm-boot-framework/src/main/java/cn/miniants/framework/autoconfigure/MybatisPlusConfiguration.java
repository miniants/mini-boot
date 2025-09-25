
package cn.miniants.framework.autoconfigure;

import cn.hutool.core.collection.CollUtil;
import cn.miniants.framework.mapper.MiniSqlInjector;
import cn.miniants.framework.web.AizudaMetaObjectHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;


@Lazy
@EnableTransactionManagement
@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfiguration {

    /**
     * mybatis-plus 插件<br>
     * 文档：http://baomidou.com
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(@Autowired(required = false) List<InnerInterceptor> innerInterceptorList) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        if (CollUtil.isNotEmpty(innerInterceptorList)) {
            // 注入自定义插件
            innerInterceptorList.forEach(interceptor::addInnerInterceptor);
        }
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public MiniSqlInjector batchSqlInjector() {
        return new MiniSqlInjector();
    }

    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public AizudaMetaObjectHandler metaObjectHandler() {
        return new AizudaMetaObjectHandler();
    }

//    @Bean
//    @ConditionalOnProperty(prefix = "mybatis-plus", name = "sql-print", havingValue = "true")
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }
}
