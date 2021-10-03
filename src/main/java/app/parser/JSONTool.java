package app.parser;

import app.log.Log;
import app.log.impl.NormalLog;
import app.system.Core;
import app.utils.SimpleUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : app.dsm.base.JSONTool
 * @Description :
 * @Date 2021-06-02 14:28:12
 * @Author ZhangHL
 */
public class JSONTool {

    private static Log log = Core.log;

    public static byte[] toJson(Object obj){
        String res = JSONObject.toJSONString(obj,false);
        return res.getBytes(StandardCharsets.UTF_8);
    }

    public static Object getObject(byte[] data,Class c){
        try {
            JSONObject obj = (JSONObject) JSON.parse(new String(data));
            Field[] fs = c.getDeclaredFields();
            Object entity = c.newInstance();
            for(Field f : fs){
                f.setAccessible(true);
                if(f.getType() == List.class){
                    JSONArray array = JSONObject.parseArray(obj.getString(f.getName()));
                    String name = f.getGenericType().getTypeName();
                    Class lp = Class.forName(name.substring(name.indexOf("<")+1,name.length()-1));
                    f.set(entity,array.toJavaList(lp));
                    continue;
                }
                if(!f.getType().getSimpleName().equals("String")){
                    f.set(entity,getObject(obj.get(f.getName()).toString().getBytes(StandardCharsets.UTF_8),f.getType()));
                    continue;
                }
                //String prop = obj.getString(f.getName());
                f.set(entity,obj.get(f.getName()));
            }
            return entity;
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error(null,e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error(null,e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getProperty(String key,byte[] data){
        JSONObject obj = (JSONObject) JSONObject.parse(new String(data));
        return  obj.getString(key);
    }
}
