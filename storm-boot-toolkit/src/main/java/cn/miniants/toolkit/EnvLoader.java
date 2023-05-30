package cn.miniants.toolkit;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class EnvLoader {

    public static void load() {
        String activeEnv = System.getenv("ACTIVE_NAMESPACE");
        log.info("=========>ACTIVE_NAMESPACE: {}", activeEnv);
        if (activeEnv == null || activeEnv.isEmpty()) {
            try {
                Properties properties = LinkedProperties.loadProperties(new ClassPathResource("dev.env"));
                properties.forEach((key, value) -> {
                    if(StrUtil.isNotBlank((String)key) && StrUtil.isNotBlank((String)value)){
                        System.setProperty(key.toString(), value.toString());
                        log.info("--->{}: {}", key, value);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Error loading dev.env", e);
            }
        }
    }
}
