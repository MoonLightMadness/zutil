package app.parser.exception;


public enum UniversalErrorCodeEnum {

    /**
     * UEC_010001 JSON字符串转换失败
     */
    UEC_010001("010001","JSON字符串转换失败"),

    /**
     * UEC_010002 属性复制失败
     */
    UEC_010002("010002","属性复制失败"),
    /**
     * UEC_010003 服务器注册失败
     */
    UEC_010003("010003","服务器注册失败"),

    /**
     * UEC_01004 文件已存在
     */
    UEC_01004("01004","文件已存在"),
    /**
     * UEC_01005 文件不存在
     */
    UEC_01005("01005","文件不存在")
    ;
    /**
     * 错误代码
     */
    private String code;

    /**
     * 错误信息
     */
    private String msg;

    UniversalErrorCodeEnum(String code, String msg){
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
