package app.config.impl;

import app.config.Config;
import app.config.enums.ConfigEnum;
import app.config.exception.ConfigException;
import com.sun.deploy.util.StringUtils;
import com.sun.org.apache.xml.internal.utils.StringVector;
import lombok.SneakyThrows;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalConfig implements Config {

    /**
     * 元信息文件，具有默认路径
     */
    private String meta = "./config/meta.txt";


    @Override
    public void setMeta(String metaFile) {
        this.meta = metaFile;
    }

    @SneakyThrows
    @Override
    public String read(String property) {
        Map<String,String> map = readAllConfigMap();
        String value = map.get(property);
        if(value == null){
            throw new ConfigException(ConfigEnum.CE_002.getCode(), ConfigEnum.CE_002.getMsg());
        }
        return value;
    }

    @SneakyThrows
    @Override
    public void write(String key, String value, String path) {
        checkFileExist(path);
        synchronized (NormalConfig.class){
            BufferedWriter writer = new BufferedWriter(new FileWriter(path,true));
            writer.newLine();
            writer.write(key+" = "+value);
            writer.flush();
            writer.close();
        }
    }

    private Map<String,String> readAllConfigMap(){
        List<String> files = this.getConfigFileList();
        Map<String,String> res = new HashMap<>();
        for (String file:files){
            checkFileExist(file);
            res.putAll(getSingleFileConfigMap(file));
        }
        return res;
    }

    @SneakyThrows
    private List<String> getConfigFileList() {
        checkMetaFileExist(meta);
        BufferedReader reader = new BufferedReader(new FileReader(meta));
        String temp;
        List<String> res= new ArrayList<>();
        while ((temp=reader.readLine())!=null){
            if(!temp.trim().equals("")){
                res.add(getValue(temp));
            }
        }
        return res;
    }

    @SneakyThrows
    private Map<String,String> getSingleFileConfigMap(String path){
        Map<String,String> res = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String temp;
        while ((temp=reader.readLine())!=null){
            if(!temp.trim().equals("")){
                res.put(getKey(temp),getValue(temp));
            }
        }
        return res;
    }

    private String getValue(String data){
        return data.split("=")[1].trim();
    }

    private String getKey(String data){
        return data.split("=")[0].trim();
    }

    @SneakyThrows
    private void checkFileExist(String meta) {
        File metaFile = new File(meta);
        if (!metaFile.exists()) {
            throw new ConfigException(ConfigEnum.CE_003.getCode(), ConfigEnum.CE_003.getMsg());
        }
    }

    @SneakyThrows
    private void checkMetaFileExist(String meta) {
        File metaFile = new File(meta);
        if (!metaFile.exists()) {
            throw new ConfigException(ConfigEnum.CE_001.getCode(), ConfigEnum.CE_001.getMsg());
        }
    }
}
