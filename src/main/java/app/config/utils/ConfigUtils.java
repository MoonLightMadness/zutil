package app.config.utils;

import app.config.Config;
import app.config.annotation.ConfigPath;
import app.config.impl.NormalConfig;

/**
 * @ClassName : app.config.utils.ConfigUtils
 * @Description :
 * @Date 2021-09-18 14:14:49
 * @Author ZhangHL
 */
public class ConfigUtils {

    /**
     * 根据Class返回Config实例
     * 与Class上的@ConfigPath有关
     * 若不存在该注解，则采用默认值
     * @param clazz clazz
     * @return @return {@link Config }
     * @author zhl
     * @date 2021-09-18 14:16
     * @version V1.0
     */
    public static Config getInstance(Class clazz){
        Config config = new NormalConfig();
        ConfigPath configPath = clazz.getClass().getAnnotation(ConfigPath.class);
        if(configPath != null){
            config.setMeta(configPath.value());
        }
        return config;
    }


}
