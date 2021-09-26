package app.game;

import app.game.domain.UserOnline;
import app.game.vo.*;

public interface UserLogService {

    UserLoginRspVO login(UserLoginReqVO userLoginReqVO);

    BaseRspVO logout(UserLogoutReqVO userLogoutReqVO);

    UserOnline getUserOnlineByTokenAndInTime(QueryUserOnlineReqVO queryUserOnlineReqVO);
}
