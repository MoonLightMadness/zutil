package app.config.impl;

import app.config.Config;
import app.config.enums.ConfigEnum;
import app.config.exception.ConfigException;
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
    private String[] meta = {"./config/meta.txt"};

    /**
     * 缓存
     * 目的在于加快配置器运行效率
     * 可使用refresh()进行刷新
     */
    private Map<String, String> cache;


    @Override
    public void setMeta(String[] metaFile) {
        this.meta = metaFile;
    }

    @SneakyThrows
    @Override
    public String read(String property) {
        if (cache == null) {
            refresh();
        }
        String value = cache.get(property);
        if (value == null) {
            throw new ConfigException(ConfigEnum.CE_002.getCode(), ConfigEnum.CE_002.getMsg());
        }
        return value;
    }

    @SneakyThrows
    @Override
    public void write(String key, String value, String path) {
        checkFileExist(path);
        synchronized (NormalConfig.class) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.newLine();
            writer.write(key + " = " + value);
            writer.flush();
            writer.close();
        }
    }

    @SneakyThrows
    @Override
    public void update(String key, String newValue, String path) {
        checkFileExist(path);
        String cpath = createCopy(path);
        String data = updateRead(key, newValue, path);
        writeToCopy(data, cpath);
        replaceWithCopy(path, cpath);
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
        cache = readAllConfigMap();
    }

    @SneakyThrows
    private String createCopy(String path) {
        File file = new File(path);
        String cpath = file.getParent() + "/" + file.getName().split("\\.")[0] + "_copy." + file.getName().split("\\.")[1];
        File copy = new File(cpath);
        copy.createNewFile();
        return cpath;
    }

    @SneakyThrows
    private String updateRead(String key, String newValue, String path) {
        StringBuilder res = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String temp;
        while ((temp = reader.readLine()) != null) {
            if (getKey(temp).equals(key)) {
                res.append(key).append(" = ").append(newValue).append("\n");
            } else {
                res.append(temp).append("\n");
            }
        }
        reader.close();
        return res.toString();
    }


    @SneakyThrows
    private void writeToCopy(String data, String path) {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
        writer.write(data);
        writer.flush();
        writer.close();
    }


    private void replaceWithCopy(String file, String copy) {
        File orin = new File(file);
        orin.delete();
        orin = new File(copy);
        orin.renameTo(new File(file));
    }


    private Map<String, String> readAllConfigMap() {
        Map<String, String> res = new HashMap<>();
        for (String file : meta) {
            checkFileExist(file);
            res.putAll(getSingleFileConfigMap(file));
        }
        return res;
    }

    @SneakyThrows
    private List<String> getConfigFileList() {
        checkMetaFileExist(meta);
        List<String> res = null;
        for (String file : meta) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String temp;
            res = new ArrayList<>();
            while ((temp = reader.readLine()) != null) {
                if (!temp.trim().equals("")) {
                    res.add(getValue(temp));
                }
            }
            reader.close();
        }
        return res;
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
        if (cut.length > 1) {
            return cut[1].trim();
        } else {
            throw new ConfigException(ConfigEnum.CE_004.getCode(), ConfigEnum.CE_004.getMsg());
        }
    }

    private String getKey(String data) {
        return data.split("=")[0].trim();
    }

    @SneakyThrows
    private void checkFileExist(String meta) {
        File metaFile = new File(meta);
        if (!metaFile.exists()) {
            throw new ConfigException(ConfigEnum.CE_003.getCode(), ConfigEnum.CE_003.getMsg()+":"+meta);
        }
    }

    @SneakyThrows
    private void checkMetaFileExist(String[] meta) {
        for (String file : meta) {
            File metaFile = new File(file);
            if (!metaFile.exists()) {
                throw new ConfigException(ConfigEnum.CE_001.getCode(), ConfigEnum.CE_001.getMsg());
            }
        }
    }
}
