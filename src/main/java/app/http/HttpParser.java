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
        return (HttpRequestEntity) JSONTool.getObject(data,HttpRequestEntity.class);
    }

    public static HttpRespondEntity parseRespondEntity(byte[] data){
        return (HttpRespondEntity) JSONTool.getObject(data,HttpRespondEntity.class);
    }


}
