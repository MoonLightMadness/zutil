package app.play;

import app.config.annotation.ConfigPath;
import app.config.impl.ConfigInitializer;

import java.util.Arrays;

/**
 * @ClassName : app.play.Application
 * @Description :
 * @Date 2021-09-18 13:32:38
 * @Author ZhangHL
 */
public class Application {
    public static void main(String[] args) {
        ConfigInitializer configInitializer = new ConfigInitializer();
        configInitializer.loadConfigPath();
        ConfigPath configPath = playTest.class.getAnnotation(ConfigPath.class);
        System.out.println(Arrays.toString(configPath.value()));
    }
}
