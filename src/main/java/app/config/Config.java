package app.config;

public interface Config {

    /**
     * 读取属性值
     *
     * @param property 属性值
     * @return
     * @author zhl
     * @date 2021-09-15 20:36
     * @version V1.0
     */
    String read(String property);


    /**
     * 写入属性
     * @param key   键
     * @param value 值
     * @param path  路径
     * @return
     * @author zhl
     * @date 2021-09-15 20:37
     * @version V1.0
     */
    void write(String key, String value, String path);


    /**
     * 设置元文件路径
     * @param metaFile 元文件
     * @return
     * @author zhl
     * @date 2021-09-15 20:40
     * @version V1.0
     */
    void setMeta(String metaFile);

}
