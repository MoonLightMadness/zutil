package app.reflect;

import app.config.Config;
import app.config.annotation.ConfigValue;
import app.config.impl.ConfigInitializer;
import app.reflect.annotation.AutoFill;
import app.reflect.annotation.Fill;
import app.reflect.annotation.Service;
import app.system.ConfigCenter;
import app.system.Core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class BeanCenter {

    private Map<String, Object> center;

    private Config config = Core.configer;

    public BeanCenter() {
        center = new HashMap<>();
    }

    public void load() {
        ConfigCenter.load();
        String[] paths = ReflectUtils.scanPackage(ConfigCenter.get("work.type"), ConfigCenter.get("bean.scan"));
        try {
            for (String path : paths) {
                Class clazz = Class.forName(path);
                if (clazz.isAnnotationPresent(Service.class)) {
                    Object instance = clazz.newInstance();
                    center.put(instance.getClass().getInterfaces()[0].getSimpleName(), instance);
                }
            }
            for (String path : paths) {
                Class clazz = Class.forName(path);
                if (clazz.isAnnotationPresent(Fill.class)) {
                    Object instance = null;
                    if (center.get(clazz.getSimpleName()) != null) {
                        instance = center.get(clazz.getSimpleName());
                    } else {
                        instance = clazz.newInstance();
                    }
                    loadFields(instance);
                    center.put(clazz.getSimpleName(), instance);
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

    public Object get(String name) {
        return center.get(name);
    }

    private void loadFields(Object obj) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean canAccess = field.isAccessible();
                field.setAccessible(true);
                if (field.isAnnotationPresent(ConfigValue.class)) {
                    ConfigValue configValue = field.getAnnotation(ConfigValue.class);
                    String value = configValue.value();
                    if (value.startsWith("${") && value.endsWith("}")) {
                        String newStr = config.read(value.substring(2, value.length() - 1));
                        //???????????????????????????????????? InvocationHandler
                        InvocationHandler h = Proxy.getInvocationHandler(configValue);
                        // ?????? AnnotationInvocationHandler ??? memberValues ??????
                        Field hField = h.getClass().getDeclaredField("memberValues");
                        // ????????????????????? private final ??????????????????????????????
                        hField.setAccessible(true);
                        // ?????? memberValues
                        Map memberValues = (Map) hField.get(h);
                        // ?????? value ?????????
                        memberValues.put("value", newStr);
                        field.set(obj, newStr);
                        Core.log.info("{}-?????????-->{}",value,newStr);
                    }
                }
                if (field.isAnnotationPresent(AutoFill.class)) {
                    field.set(obj, center.get(field.getType().getSimpleName()));
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
