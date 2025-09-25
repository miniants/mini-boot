
package cn.miniants.framework;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.miniants.framework.spring.SpringHelper;
import cn.miniants.toolkit.EnvLoader;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

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
