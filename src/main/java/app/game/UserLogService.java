package app.game;

import app.game.vo.BaseRspVO;
import app.game.vo.UserLoginReqVO;
import app.game.vo.UserLoginRspVO;
import app.game.vo.UserLogoutReqVO;

public interface UserLogService {

    UserLoginRspVO login(UserLoginReqVO userLoginReqVO);

    BaseRspVO logout(UserLogoutReqVO userLogoutReqVO);

}
