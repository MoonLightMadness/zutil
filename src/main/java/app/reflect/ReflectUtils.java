package app.reflect;


import app.utils.SimpleUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
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
    public static String[] scanPackage(String packageName) {
        String workingPath = getJarSelfPath();
        String[] paths;
        //判断程序是否是以jar包形式启动的
        if (workingPath.endsWith(".jar")) {
            paths = scanJarFile(workingPath, packageName).split("\n");
        } else {
            workingPath = ".";
            paths = scanDirectory(workingPath).split("\n");
        }
        return paths;
    }


    private static String scanDirectory(String directory) {
        File file = new File(directory);
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
            if (file.getPath().endsWith(".java")) {
                String path = file.getPath().replace(SimpleUtils.getFilePathSeparator(), ".").substring(6, file.getPath().lastIndexOf('.'));
                path = path.substring(10);
                sb.append(path).append("\n");
            }
        }
        return sb.toString();
    }

    private static String scanJarFile(String path, String packageName) {
        File f = new File(path);
        StringBuilder sb = new StringBuilder();
        try {
            JarFile jarFile = new JarFile(f);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().replace(SimpleUtils.getFilePathSeparator(), ".").startsWith(packageName)) {
                    File temp = new File(entry.getName());
                    if (entry.getName().endsWith(".class")) {
                        sb.append(temp.getPath().replace(SimpleUtils.getFilePathSeparator(), ".").substring(0, temp.getPath().lastIndexOf('.'))).append("\n");
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
