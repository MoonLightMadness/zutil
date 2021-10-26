package app.net.base;

import app.net.enums.NetCodeEnum;

public class ResponseWarpper {

    public static Response ok() {
        Response response = new Response();
        response.setCode(NetCodeEnum.NE_001.code());
        response.setMsg(NetCodeEnum.NE_001.msg());
        return response;
    }

    public static Response ok(Object obj) {
        Response response = new Response();
        response.setCode(NetCodeEnum.NE_001.code());
        response.setMsg(NetCodeEnum.NE_001.msg());
        response.setData(obj);
        return response;
    }

    public static Response error() {
        Response response = new Response();
        response.setCode(NetCodeEnum.NE_099.code());
        response.setMsg(NetCodeEnum.NE_099.msg());
        return response;
    }

    public static Response error(Object obj) {
        Response response = new Response();
        response.setCode(NetCodeEnum.NE_099.code());
        response.setMsg(NetCodeEnum.NE_099.msg());
        response.setData(obj);
        return response;
    }

    public static Response error(String code, String msg, Object obj) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(obj);
        return response;
    }
}
