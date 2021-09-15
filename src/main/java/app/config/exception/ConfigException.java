package app.config.exception;

public class ConfigException extends Exception {

    public ConfigException(String code,String msg){
        super(code+"  "+msg);
    }

}
