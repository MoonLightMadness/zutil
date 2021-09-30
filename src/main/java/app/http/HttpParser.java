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
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity();
        String method = sdata.substring(0,sdata.indexOf(" "));
        if(!"POST".equals(method)){
            return null;
        }
        httpRequestEntity.setMethod(method);
        httpRequestEntity.setArgs(sdata.substring(sdata.indexOf(" ")+1,sdata.indexOf(" ",httpRequestEntity.getMethod().length()+1)));
        //存在无数据的情况
        try {
            httpRequestEntity.setBody(sdata.substring(sdata.indexOf("{"),sdata.lastIndexOf("}")+1));
        }catch (Exception e) {
            //e.printStackTrace();
        }
        return httpRequestEntity;
    }

    public static HttpRespondEntity parseRespondEntity(byte[] data){
        String sdata = new String(data);
        HttpRespondEntity httpRespondEntity = new HttpRespondEntity();
        httpRespondEntity.setProtocol(sdata.substring(0,sdata.indexOf(" ")));
        httpRespondEntity.setBody(sdata.substring(sdata.indexOf("{"),sdata.lastIndexOf("}")+1));
        return httpRespondEntity;
    }


}
