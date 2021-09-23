package app.http.entity;

import lombok.Data;

/**
 * http响应实体
 *
 * @author zhl
 * @date 2021-09-23 19:40
 */
@Data
public class HttpRespondEntity {

    /**
     * 协议(HTTP/1.1)
     */
    private String protocol;

    /**
     * 返回码
     */
    private String respondCode;

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
