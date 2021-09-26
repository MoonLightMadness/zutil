package app.game;

import app.game.vo.UserLoginDataReqVO;
import app.game.vo.UserLogoutReqVO;
import app.game.vo.UserOfflineReqVO;

public interface UserLogDataService {

    void updateLoginData(UserLoginDataReqVO userLoginDataReqVO);

    void updateLogoffData(UserOfflineReqVO userOfflineReqVO);
}
