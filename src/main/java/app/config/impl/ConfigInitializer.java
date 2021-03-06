package app.config.impl;

import app.bind.Bind;
import app.bind.impl.ZBind;
import app.config.IConfigInitializer;
import app.config.annotation.ConfigPath;
import app.config.annotation.ConfigValue;
import app.config.utils.ConfigUtils;
import app.reflect.ReflectUtils;
import app.system.Core;
import app.utils.SimpleUtils;

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
    public void loadConfigPath(String[] packages) {
        try {
            for (String package1:packages){
                String[] allPaths = ReflectUtils.scanPackage(systemConfig.read("work.type"), package1);
                for (String path : allPaths) {
                    if(!SimpleUtils.isEmptyString(path)){
                        Class clazz = Class.forName(path);
                        ConfigPath configPath = (ConfigPath) clazz.getAnnotation(ConfigPath.class);
                        if (configPath != null) {
                            changeVariable(configPath);
                        }
                        Field[] fields = clazz.getDeclaredFields();
                        for(Field field : fields){
                            changeFieldValue(field);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException |
                NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
            Core.log.info("未找到类,原因:{}", e);
        }
    }

    private void changeVariable(ConfigPath configPath) throws IllegalAccessException, NoSuchMethodException,
            InvocationTargetException, NoSuchFieldException {
        String[] mapStrs = configPath.value();
        int count = 0;
        for (String mapStr : mapStrs) {
            if(mapStr.startsWith("${") && mapStr.endsWith("}")){
                String newStr = systemConfig.read(mapStr.substring(2,mapStr.length()-1));
                mapStrs[count++] = newStr;
            }
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

    private void  changeFieldValue(Field field) throws IllegalAccessException, NoSuchFieldException {
        boolean canAccess = field.isAccessible();
        field.setAccessible(true);
        if(field.isAnnotationPresent(ConfigValue.class)){
            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            String value = configValue.value();
            if(value.startsWith("${") && value.endsWith("}")){
                String newStr = systemConfig.read(value.substring(2,value.length()-1));
                //获取这个代理实例所持有的 InvocationHandler
                InvocationHandler h = Proxy.getInvocationHandler(configValue);
                // 获取 AnnotationInvocationHandler 的 memberValues 字段
                Field hField = h.getClass().getDeclaredField("memberValues");
                // 因为这个字段事 private final 修饰，所以要打开权限
                hField.setAccessible(true);
                // 获取 memberValues
                Map memberValues = (Map) hField.get(h);
                // 修改 value 属性值
                memberValues.put("value", newStr);
            }
        }
        field.setAccessible(canAccess);
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
