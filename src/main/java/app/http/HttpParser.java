package app.http;

import app.http.entity.HttpRequestEntity;
import app.http.entity.HttpRespondEntity;
import app.net.base.Response;
import app.net.base.ResponseWarpper;
import app.parser.JSONTool;
import app.parser.exception.ServiceException;
import app.parser.exception.UniversalErrorCodeEnum;
import app.system.Core;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;


/**
 * http解析器
 *
 * @author zhl
 * @date 2021-09-23 19:34
 */
public class HttpParser {

    @SneakyThrows
    public static Response parseRequestEntiy(byte[] data) {
        String sdata = new String(data);
        Response response = new Response();
        //如果报文体为空则自动赋大括号
        if(sdata.indexOf('{') == -1 && sdata.lastIndexOf('}') == -1){
            sdata = sdata + "{}";
        }
        if (sdata.lastIndexOf('}') != (sdata.length() - 1)) {
            Core.log.error("报文格式错误:{}",sdata);
            return ResponseWarpper.error("999999","报文格式错误",sdata);
        }
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity();
        String method = sdata.substring(0, sdata.indexOf(" "));
        if (!"POST".equals(method) && !"GET".equals(method)) {
            return null;
        }
        httpRequestEntity.setMethod(method);
        String args = sdata.substring(sdata.indexOf(" ") + 1, sdata.indexOf(" ", httpRequestEntity.getMethod().length() + 1));
        httpRequestEntity.setArgs(args.split("\\?")[0]);
        String[] subArgs = new String[0];
        if (args.indexOf('?') != -1) {
            subArgs = args.split("\\?")[1].split("&");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (String sa : subArgs) {
            builder.append("\t\"").append(sa.split("=")[0]).append("\":");
            builder.append("\"").append(sa.split("=")[1]).append("\",\n");
        }
        if (sdata.indexOf('{') != -1) {
            builder.append(sdata, sdata.indexOf("{") + 1, sdata.lastIndexOf("}") + 1);
        } else {
            builder = builder.deleteCharAt(builder.length() - 2);
            builder.append("\n}");
        }
        //存在无数据的情况
        try {
            if(isJSONValid(builder.toString())){
                httpRequestEntity.setBody(builder.toString());
            }else {
                return ResponseWarpper.error("999999","报文格式错误",sdata);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        response.setData(httpRequestEntity);
        response.setCode("000000");
        return response;
    }

    public static HttpRespondEntity parseRespondEntity(byte[] data) {
        String sdata = new String(data);
        HttpRespondEntity httpRespondEntity = new HttpRespondEntity();
        httpRespondEntity.setProtocol(sdata.substring(0, sdata.indexOf(" ")));
        httpRespondEntity.setBody(sdata.substring(sdata.indexOf("{"), sdata.lastIndexOf("}") + 1));
        return httpRespondEntity;
    }

    public static String constructRequest(HttpRequestEntity httpRequestEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(httpRequestEntity.getMethod());
        stringBuilder.append(" HTTP/1.1\n");
        stringBuilder.append("Content-Type: application/json\n\n");
        stringBuilder.append(httpRequestEntity.getBody());
        return stringBuilder.toString();
    }


    public final static boolean isJSONValid(String json) {
        try {
            JSONObject.parseObject(json);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
