package app.game;

import app.game.vo.UserLoginReqVO;
import app.game.vo.UserLoginRspVO;

public interface UserLogService {

    UserLoginRspVO login(UserLoginReqVO userLoginReqVO);

}
