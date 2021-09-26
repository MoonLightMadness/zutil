package app.game.web.frontend;


import app.game.UserDataService;
import app.game.cons.ServiceCenter;
import app.game.service.UserDataServiceImpl;
import app.game.vo.*;
import app.log.Log;
import app.reflect.annotation.Path;
import app.system.Core;

@Path("/data/user")
public class UserDataController {

    private Log log = Core.log;

    /**
     * 新用户注册
     * @param userRegisterReqVO
     * @return @return {@link UserRegisterRspVO }
     * @author zhl
     * @date 2021-09-26 21:59
     * @version V1.0
     */
    @Path("/register")
    public UserRegisterRspVO register(UserRegisterReqVO userRegisterReqVO){
        log.info("进入[注册]接口--入参:{}",userRegisterReqVO);
        UserRegisterRspVO userRegisterRspVO = ServiceCenter.userDataService.register(userRegisterReqVO);
        log.info("[入参]接口执行完毕，出参:{}",userRegisterRspVO);
        return userRegisterRspVO;
    }

    @Path("/login")
    public UserLoginRspVO login(UserLoginReqVO userLoginReqVO){
        log.info("进入[登录]接口,入参:{}",userLoginReqVO);
        UserLoginRspVO userLoginRspVO = ServiceCenter.userLogService.login(userLoginReqVO);
        log.info("[登录]接口执行完毕,出参:{}",userLoginRspVO);
        return userLoginRspVO;
    }

    @Path("/logout")
    public BaseRspVO logout(UserLogoutReqVO userLogoutReqVO){
        log.info("进入[登出]接口,入参:{}",userLogoutReqVO);
        BaseRspVO baseRspVO = ServiceCenter.userLogService.logout(userLogoutReqVO);
        return baseRspVO;
    }






}
