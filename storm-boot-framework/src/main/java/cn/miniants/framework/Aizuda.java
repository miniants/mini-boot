/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package cn.miniants.framework;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.miniants.framework.spring.SpringHelper;
import cn.miniants.toolkit.EnvLoader;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

/**
 * 爱组搭 http://aizuda.com
 *
 * @author 青苗
 * @since 2021-10-30
 */
public class Aizuda {

    /**
     * Spring Boot 启动
     *
     * @param args           运行参数
     * @param primarySources 启动类
     */
    public static void startup(String[] args, Class<?>... primarySources) {
        try {
            EnvLoader.load();

            SpringApplication application = new SpringApplication(primarySources);
            application.setBannerMode(Banner.Mode.CONSOLE);
            SpringHelper.setApplicationContext(application.run(args));
        } catch (Throwable t) {
            ExceptionUtil.stacktraceToString(t);
        }
    }
}
