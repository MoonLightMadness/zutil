package app.game.exception;

/**
 * @ClassName : app.game.exception.UserException
 * @Description :
 * @Date 2021-09-29 16:53:21
 * @Author ZhangHL
 */
public class UserException extends Exception{

    public UserException(String id,String msg){
        super(id+" "+msg);
    }


}
