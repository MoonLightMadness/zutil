package app.bind.enums;

/**
 * @ClassName : app.bind.enums.BindEnums
 * @Description :
 * @Date 2021-09-16 14:18:43
 * @Author ZhangHL
 */
public enum BindEnums {

    /**
     * 001 指定Key不存在
     */
    BE_001("001","指定Key不存在"),

    ;


    /**
     * 错误代码
     */
    private String code;

    /**
     * 错误信息
     */
    private String msg;

    BindEnums(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
