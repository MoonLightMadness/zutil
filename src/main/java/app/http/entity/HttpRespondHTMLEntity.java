package app.http.entity;

import lombok.Data;

/**
 * @ClassName : app.http.entity.HttpRespondHTMLEntity
 * @Description :
 * @Date 2021-10-28 09:27:05
 * @Author ZhangHL
 */
@Data
public class HttpRespondHTMLEntity {
    /**
     * 协议(HTTP/1.1)
     */
    private String protocol = "HTTP/1.1";

    /**
     * 返回码
     */
    private String respondCode = "200";

    private String userAgent;

    /**
     * 内容类型(application/json)
     */
    private String contentType = "text/html; charset=utf-8";

    private String host;

    /**
     * 连接(Keep-Alive)
     */
    private String connection;

    /**
     * 报文体
     */
    private String body;

    /**
     * 同意任何跨源请求
     */
    private String ACAO = "Access-Control-Allow-Origin:*";

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(protocol).append(" ").append(respondCode).append("\n");
        stringBuilder.append("Content-Type: ").append(contentType).append("\n");
        stringBuilder.append(ACAO).append("\n");
        stringBuilder.append("\n");
        stringBuilder.append(body);
        return stringBuilder.toString();
    }
}
