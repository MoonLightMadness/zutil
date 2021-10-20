package app.net.enums;

import app.reflect.enums.AuthorityEnum;

public enum NetCodeEnum {

    NE_001("000000","成功"),


    NE_099("999999","失败")
    ;

    private String code;
    private String msg;

    public String msg() {
        return msg;
    }

    public String code() {
        return code;
    }

    NetCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static NetCodeEnum getEnum(int code) {
        for (NetCodeEnum ele : NetCodeEnum.values()) {
            if (ele.code() .equals(code) ) {
                return ele;
            }
        }
        return null;
    }

    public static NetCodeEnum getByMsg(String msg){
        for (NetCodeEnum ae : NetCodeEnum.values()){
            if(ae.msg.equals(msg)){
                return ae;
            }
        }
        return null;
    }


}
