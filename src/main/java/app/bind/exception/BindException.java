package app.bind.exception;

import app.bind.Bind;

/**
 * @ClassName : app.bind.exception.BindException
 * @Description :
 * @Date 2021-09-16 14:17:49
 * @Author ZhangHL
 */
public class BindException extends Exception{

    public BindException(String code,String msg){
        super(code+" "+msg);
    }

}
