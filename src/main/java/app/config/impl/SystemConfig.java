package app.config.impl;

import app.config.Config;
import app.config.enums.ConfigEnum;
import app.config.exception.ConfigException;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : app.config.impl.SystemConfig
 * @Description :
 * @Date 2021-09-18 10:35:39
 * @Author ZhangHL
 */
public class SystemConfig implements Config {

    private String[] meta = {"./config/sys.txt"};

    Map<String, String> sysCache;

    /**
     * 读取属性值
     *
     * @param property 属性值
     * @return
     * @author zhl
     * @date 2021-09-15 20:36
     * @version V1.0
     */
    @Override
    public String read(String property) {
        checkMetaFileExist(meta[0]);
        return getSingleFileConfigMap(meta[0]).get(property);
    }

    @SneakyThrows
    private void checkMetaFileExist(String meta) {
        File metaFile = new File(meta);
        if (!metaFile.exists()) {
            throw new ConfigException(ConfigEnum.CE_001.getCode(), ConfigEnum.CE_001.getMsg());
        }
    }

    @SneakyThrows
    private Map<String, String> getSingleFileConfigMap(String path) {
        Map<String, String> res = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String temp;
        while ((temp = reader.readLine()) != null) {
            if (!temp.trim().equals("")) {
                res.put(getKey(temp), getValue(temp));
            }
        }
        reader.close();
        return res;
    }

    @SneakyThrows
    private String getValue(String data) {
        String[] cut = data.split("=");
        if(cut.length > 1){
            return cut[1].trim();
        }else {
            throw new ConfigException(ConfigEnum.CE_004.getCode(),ConfigEnum.CE_004.getMsg());
        }
    }

    private String getKey(String data) {
        return data.split("=")[0].trim();
    }

    /**
     * 写入属性
     *
     * @param key   键
     * @param value 值
     * @param path  路径
     * @return
     * @author zhl
     * @date 2021-09-15 20:37
     * @version V1.0
     */
    @Override
    public void write(String key, String value, String path) {

    }

    /**
     * 更新属性
     *
     * @param key      键
     * @param newValue 值
     * @param path     路径
     * @return
     * @author zhl
     * @date 2021-09-15 20:37
     * @version V1.0
     */
    @Override
    public void update(String key, String newValue, String path) {

    }

    /**
     * 设置元文件路径
     *
     * @param metaFile 元文件
     * @return
     * @author zhl
     * @date 2021-09-15 20:40
     * @version V1.0
     */
    @Override
    public void setMeta(String[] metaFile) {
        this.meta = metaFile;
    }

    /**
     * 刷新缓存
     *
     * @return
     * @author zhl
     * @date 2021-09-18 09:49
     * @version V1.0
     */
    @Override
    public void refresh() {

    }
}
