package app.http;

import app.http.entity.HttpRequestEntity;
import app.http.entity.HttpRespondEntity;
import app.parser.JSONTool;


/**
 * http解析器
 *
 * @author zhl
 * @date 2021-09-23 19:34
 */
public class HttpParser {

    public static HttpRequestEntity parseRequestEntiy(byte[] data){
        String sdata = new String(data);
        String mode = sdata.substring(0,sdata.indexOf(" "));
        String path = sdata.substring(sdata.indexOf(" ")+1,sdata.indexOf(" ",mode.length()+1));
        String body = null;
        //存在无数据的情况
        try {
            body = sdata.substring(sdata.indexOf("{"),sdata.lastIndexOf("}")+1);
        }catch (Exception e) {
            //e.printStackTrace();
        }
        return (HttpRequestEntity) JSONTool.getObject(data,HttpRequestEntity.class);
    }

    public static HttpRespondEntity parseRespondEntity(byte[] data){
        return (HttpRespondEntity) JSONTool.getObject(data,HttpRespondEntity.class);
    }


}
