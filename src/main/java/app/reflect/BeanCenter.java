package app.reflect;

import app.config.Config;
import app.config.annotation.ConfigValue;
import app.config.impl.ConfigInitializer;
import app.reflect.annotation.Fill;
import app.system.Core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanCenter {

    private Map<String,Object> center;

    private Config config = Core.configer;

    public BeanCenter(){
        center = new HashMap<>();
    }

    public void load(){
        ConfigInitializer configInitializer = new ConfigInitializer();
        configInitializer.loadConfigPath(new String[]{config.read("bean.scan")});
        String[] paths = ReflectUtils.scanPackage(config.read("bean.scan"));
        try {
            for (String path:paths){
                Class clazz = Class.forName(path);
                if(clazz.isAnnotationPresent(Fill.class)){
                    Object instance = clazz.newInstance();
                    loadFields(instance);
                    center.put(clazz.getSimpleName(),instance);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object get(String name){
        return center.get(name);
    }

    private void loadFields(Object obj){
        try {
            Field[] fields = obj.getClass().getFields();
            for (Field field : fields){
                boolean canAccess = field.isAccessible();
                field.setAccessible(true);
                if(field.isAnnotationPresent(ConfigValue.class)){
                    ConfigValue configValue = field.getAnnotation(ConfigValue.class);
                    String value = configValue.value();
                    if(value.startsWith("${") && value.endsWith("}")){
                        String newStr = config.read(value.substring(2,value.length()-1));
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
                        field.set(obj,newStr);
                    }
                }
                field.setAccessible(canAccess);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}
