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

    public static HttpRequestEntity parseRequestEntiy(byte[] data) {
        String sdata = new String(data);
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
            System.out.println(builder.toString());
            httpRequestEntity.setBody(builder.toString());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return httpRequestEntity;
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

}
