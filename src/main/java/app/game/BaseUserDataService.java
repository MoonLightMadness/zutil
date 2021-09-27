package app.game;

import app.game.domain.UserData;

/**
 * @ClassName : app.game.BaseUserDataService
 * @Description :
 * @Date 2021-09-27 08:23:05
 * @Author ZhangHL
 */
public interface BaseUserDataService {

    UserData getUserDataById(String userId);

    UserData getUserDataByUserLogInfo(String userName,String userPassword);

}
