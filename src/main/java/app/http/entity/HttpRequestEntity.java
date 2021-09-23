package app.http.entity;

import lombok.Data;

@Data
public class HttpRequestEntity {

    /**
     * POST PUT GET
     */
    private String method;

    /**
     * 协议(HTTP/1.1)
     */
    private String protocol;

    /**
     * 参数
     */
    private String args;

    private String userAgent;

    /**
     * 内容类型(application/json)
     */
    private String contentType;

    private String host;

    /**
     * 连接(Keep-Alive)
     */
    private String connection;

    /**
     * 报文体
     */
    private String body;

}
