package app.config.impl;

import app.bind.Bind;
import app.bind.impl.ZBind;
import app.config.IConfigInitializer;
import app.config.annotation.ConfigPath;
import app.reflect.ReflectUtils;
import app.system.Core;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        String[] allPaths = ReflectUtils.scanPackage("app");
        try {
            for (String path : allPaths){
                Class clazz = Class.forName(path);
                Annotation annotation = clazz.getAnnotation(ConfigPath.class);
                if(annotation != null){
                    changeVariable(annotation);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            Core.log.info("未找到类,原因:{}",e);
        }
    }

    private void changeVariable(Annotation annotation) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, NoSuchFieldException {
        ConfigPath configPath = (ConfigPath) annotation;
        String[] mapStrs = configPath.value();
        int count = 0;
        for (String mapStr : mapStrs){
            String newStr = systemConfig.read(mapStr);
            mapStrs[count++] = newStr;
        }
        //获取这个代理实例所持有的 InvocationHandler
        InvocationHandler h = Proxy.getInvocationHandler(configPath);
        // 获取 AnnotationInvocationHandler 的 memberValues 字段
        Field hField = h.getClass().getDeclaredField("memberValues");
        // 因为这个字段事 private final 修饰，所以要打开权限
        hField.setAccessible(true);
        // 获取 memberValues
        Map memberValues = (Map) hField.get(h);
        // 修改 value 属性值
        memberValues.put("value", mapStrs);
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
