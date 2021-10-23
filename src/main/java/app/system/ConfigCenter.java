package app.system;

import app.log.Log;
import app.utils.SimpleUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigCenter {

    private static Log log = Core.log;

    private static Map<String, String> config = new HashMap<>();

    private static boolean isLoaded = false;

    public static String get(String config) {
        return ConfigCenter.config.get(config);
    }

    public static void add(String key, String value) {
        ConfigCenter.config.put(key, value);
    }

    public static void load() {
        if(!isLoaded){
            log.info("初始化ConfigCenter");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(SystemConstant.CONFIG_FILE_PATH));
                String temp;
                int equalIndex;
                String key;
                String value;
                while ((temp = reader.readLine()) != null) {
                    if (!temp.trim().equals("")) {
                        equalIndex = temp.indexOf('=');
                        key = temp.substring(0, equalIndex).trim();
                        value = temp.substring(equalIndex + 1).trim();
                        ConfigCenter.config.put(key, value);
                        log.info("配置项:<{}>已加载->{}",key,value);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isLoaded = true;
        log.info("{}初始化完成","ConfigCenter");
    }
}
