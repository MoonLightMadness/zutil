package app.config.impl;

import app.bind.Bind;
import app.bind.impl.ZBind;
import app.config.IConfigInitializer;
import app.config.annotation.ConfigPath;
import app.reflect.ReflectUtils;
import app.system.Core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @ClassName : app.config.impl.ConfigInitializer
 * @Description :
 * @Date 2021-09-18 10:02:31
 * @Author ZhangHL
 */
public class ConfigInitializer implements IConfigInitializer {


    SystemConfig systemConfig = new SystemConfig();

    /**
     * 扫描程序以加载配置路径
     *
     * @return
     * @author zhl
     * @date 2021-09-18 09:59
     * @version V1.0
     */
    @Override
    public void loadConfigPath() {
        String[] allPaths = ReflectUtils.scanPackage(".");
        try {
            for (String path : allPaths){
                Class clazz = Class.forName(path);
                Annotation annotation = clazz.getAnnotation(ConfigPath.class);
                if(annotation != null){
                    changeVariable(annotation);
                }
            }
        } catch (ClassNotFoundException e) {
            Core.log.info("未找到类,原因:{}",e);
        }
    }

    private void changeVariable(Annotation annotation){
        ConfigPath configPath = (ConfigPath) annotation;
        String[] mapStrs = configPath.value();
        int count = 0;
        for (String mapStr : mapStrs){
            mapStrs[count++] = systemConfig.read(mapStr);
        }
    }

    /**
     * 从给的路径中加载配置
     *
     * @return
     * @author zhl
     * @date 2021-09-18 10:01
     * @version V1.0
     */
    @Override
    public void loadConfigFromPath() {

    }
}
