package app.config.enums;

public enum ConfigEnum {

    /**
     * 001 指定文件不存在
     */
    CE_001("001","元文件不存在"),
    /**
     * 002 指定属性不存在
     */
    CE_002("002","指定属性不存在"),
    /**
     * 003 指定文件不存在
     */
    CE_003("003","指定文件不存在"),

    ;
    private String code;

    private String msg;

     ConfigEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
