package cn.miniants.toolkit;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class EnvLoader {

    public static void load() {
        String activeEnv = System.getProperty("ACTIVE_NAMESPACE");
        if (activeEnv == null || activeEnv.isEmpty()) {
            try {
                Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("dev.env"));
                properties.forEach((key, value) -> {
                    if(StrUtil.isNotBlank((String)key) && StrUtil.isNotBlank((String)value)){
                        System.setProperty(key.toString(), value.toString());
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Error loading dev.env", e);
            }
        }
    }
}
