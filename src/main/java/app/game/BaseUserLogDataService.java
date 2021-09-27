package app.game;

import app.game.domain.UserLogData;

import java.util.List;

/**
 * @ClassName : app.game.BaseUserLogDataService
 * @Description :
 * @Date 2021-09-27 08:37:49
 * @Author ZhangHL
 */
public interface BaseUserLogDataService {

    List<UserLogData> getUserLogDataByUserId(String userId);

}
