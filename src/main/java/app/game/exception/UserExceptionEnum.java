package app.game.exception;

public enum UserExceptionEnum {

    /**
     * uee 001 当前logToken无效
     */
    UEE_001("001","当前logToken无效"),

    ;

    private String id;
    private String msg;

    UserExceptionEnum(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }
}
