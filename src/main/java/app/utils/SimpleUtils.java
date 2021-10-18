package app.utils;


import app.config.annotation.ConfigValue;
import app.log.Log;

import app.log.impl.NormalLog;
import app.parser.exception.ServiceException;
import app.parser.exception.UniversalErrorCodeEnum;
import app.parser.impl.JSONParserImpl;

import app.reflect.annotation.Fill;
import app.system.Core;
import app.utils.ds.XByteBuffer;
import lombok.SneakyThrows;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单工具类
 *
 * @ClassName : utils.base.simpleUtils
 * @Description :
 * @Date 2021-03-31 20:11:49
 * @Author ZhangHL
 */
@Fill
public class SimpleUtils {

    private static Log log = Core.log;


    /**
     * 判断字符串是否为空
     *
     * @return 是则返回true，否则返回false
     * @class SimpleUtils
     * @Description //TODO
     * @Param
     * @Author Zhang huai lan
     * @Date 20:20 2021/3/31
     **/
    public static boolean isEmptyString(String str) {
        return (str == null || "".equals(str));
    }

    /**
     * 固定使用TimeFormatter.SEC_LEVEL
     *
     * @return
     * @class *ClassName*
     * @Description //TODO
     * @Param
     * @Author Zhang huai lan
     * @Date 21:23 2021/3/31
     **/
    public static String getTimeStamp() {
        return new SimpleDateFormat(TimeFormatter.SEC_LEVEL).format(new Date());
    }


    /**
     * 序列化
     *
     * @return byte数组
     * @class
     * @Param
     * @Author Zhang huai lan
     * @Date 8:30 2021/4/14
     * @version V1.0
     **/
    public static byte[] serializableToBytes(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] res = baos.toByteArray();
            baos.close();
            oos.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] serializableToBase64(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.writeObject(null);
            byte[] res = baos.toByteArray();
            baos.close();
            oos.close();
            return Base64.getEncoder().encode(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @return Object对象
     * @class
     * @Param
     * @Author Zhang huai lan
     * @Date 8:30 2021/4/14
     * @version V1.0
     **/
    public static Object bytesToSerializableObject(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object obj;
            while ((obj = ois.readObject()) != null) {
                bais.close();
                ois.close();
                return obj;
            }
            //Object obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将普通字符串转为Base64字符串
     */
    public static String string2Base64Str(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 将Base64字符串转化为普通字符串
     */
    public static String base64Str2String(String base64Str) {
        return new String(Base64.getDecoder().decode(base64Str.getBytes(StandardCharsets.UTF_8)));
    }


    /**
     * 调用Shell <br>
     * <p>
     * c 是执行完dir命令后关闭命令窗口。<br>
     * k 是执行完dir命令后不关闭命令窗口。<br>
     * c start 会打开一个新窗口后执行dir指令，原窗口会关闭。<br>
     * k start 会打开一个新窗口后执行dir指令，原窗口不会关闭。<br>
     *
     * @param cmd    cmd
     * @param option 选项
     * @param block  是否阻塞直到运行完毕
     * @return {@link String} 返回值
     */
    public static String callShell(String cmd, String option, boolean block) {
        try {
            Process p;
            p = Runtime.getRuntime().exec("cmd /" + option + " " + cmd);
            if (block) {
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));
                String temp;
                StringBuilder builder = new StringBuilder();
                while ((temp = reader.readLine()) != null) {
                    builder.append(temp).append("\n");
                }
                return builder.toString();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行Bash文件
     *
     * @param bashPath bash路径
     * @return @return {@link String }
     * @author zhl
     * @date 2021-09-20 01:00
     * @version V1.0
     */
    public static String callBash(String bashPath) {
        try {
            Process p;
            p = Runtime.getRuntime().exec(bashPath);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("gbk")));
            String temp;
            StringBuilder builder = new StringBuilder();
            while ((temp = reader.readLine()) != null) {
                builder.append(temp).append("\n");
            }
            p.destroy();
            return builder.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将字节数组列表合并成一个字节数组
     *
     * @param list  列表
     * @param count 总长度
     * @return {@link byte[]}
     */
    public static byte[] mergeByteList(List<byte[]> list, int count) {
        byte[] res = new byte[count];
        int pointer = 0;
        for (byte[] b : list) {
            if (pointer >= count) {
                break;
            }
            System.arraycopy(b, 0, res, pointer, b.length);
            pointer += b.length;
        }
        return res;
    }

    public static byte[] receiveDataInNIO(SocketChannel socketChannel) {
        int standard = 1024;
        XByteBuffer xb = new XByteBuffer();
        ByteBuffer buffer = ByteBuffer.allocate(standard);
        int size;
        while (true) {
            buffer.clear();
            try {
                size = socketChannel.read(buffer);
                if (size > 0) {
                    if (size == standard) {
                        buffer.flip();
                        xb.append(buffer.array());
                    } else {
                        byte[] rb = new byte[size];
                        buffer.flip();
                        System.arraycopy(buffer.array(), 0, rb, 0, size);
                        xb.append(rb);
                    }
                }
                buffer.clear();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    socketChannel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            }
            if (size <= 0) {
                break;
            }
        }
        return xb.getBytes();
    }


    public static byte[] readFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            log.error("can not find {}", path);
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();
            return content;
        } catch (FileNotFoundException e) {
            log.error(null, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(String path, byte[] content) {
        File file = new File(path);
        if (!file.exists()) {
            log.error("can not find {}", path);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(content);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            log.error(null, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算从0000-01-01到给定时间段的天数
     *
     * @param year  一年
     * @param month 月
     * @param day   一天
     * @return @return long
     * @author zhl
     * @date 2021/08/09
     * @version V1.0
     */
    public static long calEpochDay(long year, long month, long day) {
        long y = year;
        long m = month;
        long total = 0;
        total += 365 * y;
        if (y >= 0) {
            total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400;
        } else {
            total -= y / -4 - y / -100 + y / -400;
        }
        total += ((367 * m - 362) / 12);
        total += day - 1;
        if (m > 2) {
            total--;
            if (!isLeapYear(year)) {
                total--;
            }
        }
        return total;
    }

    private static boolean isLeapYear(long prolepticYear) {
        return ((prolepticYear & 3) == 0) && ((prolepticYear % 100) != 0 || (prolepticYear % 400) == 0);
    }

    /**
     * 复制一个新的对象
     *
     * @param obj obj
     * @return @return {@link Object }
     * @author zhl
     * @date 2021-08-11 10:12
     * @version V1.0
     */
    @SneakyThrows
    public static Object duplicate(Object obj) {
        try {
            log.info("属性复制开始，入参:{}", obj);
            Class clazz = obj.getClass();
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object newer = constructor.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(obj).getClass().isPrimitive() || (field.get(obj).getClass() == String.class)
                        || (field.get(obj).getClass() == Integer.class)) {
                    field.set(newer, field.get(obj));
                } else {
                    field.set(newer, duplicate(field.get(obj)));
                }
            }
            return newer;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("属性复制失败，原因:{}", e);
            throw new ServiceException(UniversalErrorCodeEnum.UEC_010002.getCode(), UniversalErrorCodeEnum.UEC_010002.getMsg());

        }

    }

    public static String getFilePathSeparator() {
        return System.getProperty("file.separator");
    }

    public static void copyProperties(Object src, Object tgt) {
        try {
            Field[] srcFileds = src.getClass().getDeclaredFields();
            Field[] tgtFileds = tgt.getClass().getDeclaredFields();
            for (Field field : tgtFileds) {
                boolean isAccess = field.isAccessible();
                field.setAccessible(true);
                for (Field sFiled : srcFileds) {
                    boolean sIsAccess = sFiled.isAccessible();
                    sFiled.setAccessible(true);
                    if (field.getName().equals(sFiled.getName())) {
                        field.set(tgt, sFiled.get(src));
                    }
                    sFiled.setAccessible(sIsAccess);
                }
                field.setAccessible(isAccess);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public boolean hasClassAnnotation(Class clazz, Class annotationType) {
        Annotation annotation = clazz.getDeclaredAnnotation(annotationType);
        if (null != annotation && annotation.annotationType() == annotationType) {
            return true;
        }
        return false;
    }

    public boolean hasMethodAnnotaion(Method method, Class annotationType) {
        Annotation annotation = method.getDeclaredAnnotation(annotationType);
        if (null != annotation && annotation.annotationType() == annotationType) {
            return true;
        }
        return false;
    }

    /**
     * 获取类级别注解中value的值
     *
     * @param clazz          clazz
     * @param annotationType 注释类型
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-23 14:00
     * @version V1.0
     */
    public String getClassAnnotationValue(Class clazz, Class annotationType) {
        if (hasClassAnnotation(clazz, annotationType)) {
            Annotation annotation = clazz.getDeclaredAnnotation(annotationType);
            try {
                Method method = annotation.getClass().getDeclaredMethod("value");
                return (String) method.invoke(annotation);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("获取注解值失败，原因：{}", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取方法级别的注解的value的值
     *
     * @param method         方法
     * @param annotationType 注释类型
     * @return @return {@link String }
     * @author zhl
     * @date 2021-08-23 14:33
     * @version V1.0
     */
    public String getMethodAnnotationValue(Method method, Class annotationType) {
        if (hasMethodAnnotaion(method, annotationType)) {
            Annotation annotation = method.getDeclaredAnnotation(annotationType);
            try {
                Method me = annotation.getClass().getDeclaredMethod("value");
                return (String) me.invoke(annotation);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.error("获取注解值失败，原因：{}", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 快速排序
     *
     * @param array 数组
     * @param start 开始
     * @param end   结束
     * @return @return {@link int[] }
     * @author zhl
     * @date 2021-08-23 15:13
     * @version V1.0
     */
    public static int[] qsort(int array[], int start, int end) {
        int pivot = array[start];
        int i = start;
        int j = end;
        while (i < j) {
            while ((i < j) && (array[j] > pivot)) {
                j--;
            }
            while ((i < j) && (array[i] < pivot)) {
                i++;
            }
            if ((array[i] == array[j]) && (i < j)) {
                i++;
            } else {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        if (i - 1 > start) {
            array = qsort(array, start, i - 1);
        }
        if (j + 1 < end) {
            array = qsort(array, j + 1, end);
        }
        return array;
    }

    /**
     * 对对象数值进行排序
     * 排序标志：Field
     *
     * @param array 数组
     * @param field 排序标志，该字段类型必须为数值类型
     * @param start 开始
     * @param end   结束
     * @return @return {@link Object[] }
     * @author zhl
     * @date 2021-08-23 15:51
     * @version V1.0
     */
    public static Object[] qsortObjects(Object[] array, Field field, int start, int end) {
        long pivot = parseToLong(array[start], field);
        int i = start;
        int j = end;
        while (i < j) {
            while ((i < j) && (parseToLong(array[j], field) > pivot)) {
                j--;
            }
            while ((i < j) && (parseToLong(array[i], field) < pivot)) {
                i++;
            }
            if ((parseToLong(array[i], field) == parseToLong(array[j], field)) && (i < j)) {
                i++;
            } else {
                Object temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        if (i - 1 > start) {
            array = qsortObjects(array, field, start, i - 1);
        }
        if (j + 1 < end) {
            array = qsortObjects(array, field, j + 1, end);
        }
        return array;
    }

    /**
     * 对对象数值进行排序
     * 排序标志：Field
     *
     * @param objects 对象
     * @param field   具有基本数值类型的字段
     * @return @return {@link List<Object> }
     * @author zhl
     * @date 2021-08-23 16:08
     * @version V1.0
     */
    public static List<Object> qsortObjects(List<Object> objects, Field field) {
        Object[] os = objects.toArray();
        os = qsortObjects(os, field, 0, os.length - 1);
        return Arrays.asList(os);
    }

    private static long parseToLong(Object object, Field field) {
        return Long.valueOf(String.valueOf(getObjectFieldValue(object, field)));
    }

    public static Object getObjectFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("获取字段值失败，原因:{}", e);
            e.printStackTrace();
        }
        return null;
    }

    public static String[] addressCutter(String address) {
        // example: /127.0.0.1:10000
        address = address.substring(1);
        String[] result = new String[2];
        result[0] = address.split(":")[0];
        result[1] = address.split(":")[1];
        return result;
    }

    public static Object parseTo(byte[] json, Class clazz) {
        return new JSONParserImpl().parser(json, clazz);
    }

    /**
     * 终结Java服务 <br/>
     * windows系统使用
     *
     * @param serviceName 服务名称
     * @return @return {@link String }
     * @author zhl
     * @date 2021-09-06 13:46
     * @version V1.0
     */
    public static String terminateJavaService(String serviceName) {
        String str = "jps | find /I \"" + serviceName + "\"";
        String res = SimpleUtils.callShell(str, "c", true);
        res = res.split(" ")[0].trim();
        res = SimpleUtils.callShell("taskkill /F /PID " + res, "c", true);
        return res;
    }

    /**
     * 根据给定字符串打印列表对象 <br>
     *
     * @param objects 对象
     * @param method  方法
     * @return @return {@link String }
     * @author zhl
     * @date 2021-10-18 10:36
     * @version V1.0
     */
    public static <T> String printList(List<T> objects, String method) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] methods = method.split(",");
        try {
            for (Object obj : objects) {
                for (String m : methods) {
                    Field field = obj.getClass().getDeclaredField(m);
                    boolean canAccess = field.isAccessible();
                    field.setAccessible(true);
                    stringBuilder.append(m).append("=").append(field.get(obj)).append("  ");
                    field.setAccessible(canAccess);
                }
                stringBuilder.append("\n");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取指定数量的空格
     *
     * @param num
     * @return @return {@link String }
     * @author zhl
     * @date 2021-10-18 10:58
     * @version V1.0
     */
    public static String getSpaces(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    /**
     * 打印列表信息
     *
     * @param objects 对象
     * @return @return {@link String }
     * @author zhl
     * @date 2021-10-18 10:37
     * @version V1.0
     */
    public static <T> String printList(List<T> objects) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objects) {
            sb.append(obj).append("\n");
        }
        return sb.toString();
    }

    @ConfigValue("${utils.savePath}")
    private static String savePath;

    /**
     * @param source   文件源
     * @param fileName 保存文件的名称
     * @return @return {@link String }
     * @author zhl
     * @date 2021-10-18 22:45
     * @version V1.0
     */
    @SneakyThrows
    public static String moveFile(String source, String fileName) {
        File file = new File(savePath + fileName);
        if (file.exists()) {
            throw new ServiceException(UniversalErrorCodeEnum.UEC_01004.getCode(), UniversalErrorCodeEnum.UEC_01004.getMsg());
        }
        File src = new File(source);
        if (!src.exists()) {
            throw new ServiceException(UniversalErrorCodeEnum.UEC_01005.getCode(), UniversalErrorCodeEnum.UEC_01005.getMsg());
        }
        src.renameTo(file);
        return savePath + fileName;
    }

}
