package app.reflect;


import app.reflect.annotation.Authority;
import app.reflect.annotation.Fill;
import app.reflect.annotation.Path;
import app.reflect.annotation.Service;
import app.reflect.container.Indicators;
import app.reflect.container.ServiceFiller;
import app.reflect.domain.ReflectIndicator;
import app.reflect.domain.ServiceInfo;
import app.reflect.enums.AuthorityEnum;
import app.system.Core;
import app.utils.SimpleUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName : app.utils.net.NetUtils
 * @Description :
 * @Date 2021-09-02 10:56:04
 * @Author ZhangHL
 */
public class ReflectUtils {


    /**
     * 扫描指定路径下的.java的所有文件或者指定jar包中的所有.class文件
     *
     * @param packageName 包名
     * @return @return {@link String[] }
     * @author zhl
     * @date 2021-09-16 09:05
     * @version V1.0
     */
    public static String[] scanPackage(String workingType, String packageName) {
        String workingPath = packageName;
        if (workingType.toLowerCase(Locale.ROOT).equals("jar")) {
            workingPath = getJarSelfPath();
            System.out.println("Running in JAR file...");
            System.out.println("Working in : " + workingPath);
        }
        String[] paths;
        //判断程序是否是以jar包形式启动的
        if (workingPath.endsWith(".jar")) {
            paths = scanJarFile(workingPath, packageName).split("\n");
        } else {
            workingPath = packageName.replace(".", "\\") + "/";
            paths = scanDirectory(workingPath).split("\n");
        }
        return paths;
    }


    private static String scanDirectory(String directory) {
        File file = new File("./target/classes/" + directory);
        StringBuilder sb = new StringBuilder();
        if (!file.exists()) {
            return sb.toString();
        }
        if (file.isDirectory()) {
            String[] files = file.list();
            for (String f : files) {
                sb.append(scanDirectory(directory + "/" + f));
            }
        } else {
            if (file.getPath().endsWith(".java") || file.getPath().endsWith(".class")) {
                String path = file.getPath().replace(SimpleUtils.getFilePathSeparator(), ".");
                path = path.split("classes")[1].substring(1);
                path = path.substring(0, path.lastIndexOf("."));
                sb.append(path).append("\n");
            }
        }
        return sb.toString();
    }


    /**
     * 搜索给定包名下的所有类，并找出具有@Path注解的类以及该类下同样具有该注解的方法，数据存入容器中
     *
     * @param packageName 包名
     * @param indicator   反射容器
     * @return
     * @author zhl
     * @date 2021-09-24 15:15
     * @version V1.0
     */
    public static void constructReflectIndicator(String packageName, Indicators indicator) {
        ReflectIndicator temp = null;
        String[] classes = ReflectUtils.scanPackage(Core.configer.read("work.type"), packageName);
        Class clazz = null;
        for (String className : classes) {
            try {
                clazz = Class.forName(className);
            } catch (Exception e) {

            }
            if (clazz != null && clazz.isAnnotationPresent(Path.class)) {
                Path classPath = (Path) clazz.getDeclaredAnnotation(Path.class);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(Path.class)) {
                        Path methodPath = method.getDeclaredAnnotation(Path.class);
                        temp = new ReflectIndicator();
                        temp.setClassPath(className);
                        temp.setMethodName(method.getName());
                        temp.setParameterTypes(generateParameters(method));
                        temp.setRelativePath(classPath.value() + methodPath.value());
                        //判断有无权限注解
                        if (method.isAnnotationPresent(Authority.class)) {
                            Authority authority = method.getDeclaredAnnotation(Authority.class);
                            temp.setAuthority(authority.value());
                        } else {
                            //如果没有权限注解,则添加默认权限
                            temp.setAuthority(AuthorityEnum.NORMAL.msg());
                        }
                        indicator.add(temp);
                        System.out.println(temp);
                    }
                }
            }
        }
    }


    private static String[] generateParameters(Method method) {
        String[] paras = new String[method.getParameterCount()];
        int count = 0;
        for (Class clazz : method.getParameterTypes()) {
            paras[count++] = clazz.getName();
        }
        return paras;
    }


    private static String scanJarFile(String path, String packageName) {
        File f = new File(path);
        StringBuilder sb = new StringBuilder();
        System.out.println("Scaning in " + packageName);
        try {
            JarFile jarFile = new JarFile(f);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if ((entry.getName().replace(SimpleUtils.getFilePathSeparator(), ".").startsWith(packageName))) {
                    File temp = new File(entry.getName());
                    if (entry.getName().endsWith(".class")) {
                        String p = temp.getPath().replace(SimpleUtils.getFilePathSeparator(), ".").substring(0, temp.getPath().lastIndexOf('.'));
                        sb.append(p).append("\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 获取以Jar形式包启动的自身路径(注意不是获取工作路径)
     * 该方法在以ide启动时会返回工作路径
     *
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-12 23:10
     * @version V1.0
     */
    private static String getJarSelfPath() {
        URL url = SimpleUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            //转化为utf-8编码，支持中文
            path = URLDecoder.decode(url.getPath(), "utf-8");
            return path;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
