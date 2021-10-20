package app.net.base;


import lombok.Data;

@Data
public class Response {

    private String code;

    private String msg;

    private Object data;

}
