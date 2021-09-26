package app.game;

import app.game.vo.*;

public interface UserDataService {


    UserRegisterRspVO register(UserRegisterReqVO userRegisterReqVO);

    UserQueryPasswordRspVO getPassword(UserQueryPasswordReqVO userQueryPasswordReqVO);

    UserQueryUserIdRspVO getUserId(UserQueryUserIdReqVO userQueryUserIdReqVO);

}
