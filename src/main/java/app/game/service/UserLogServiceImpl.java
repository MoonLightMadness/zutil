package app.game.service;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.game.UserLogService;
import app.game.cons.ServiceCenter;
import app.game.vo.*;
import app.mapper.annotation.TableName;
import app.mapper.impl.Mapper;
import app.utils.SimpleUtils;
import app.utils.guid.impl.SnowFlake;

import java.time.LocalDateTime;

@TableName("user_online")
public class UserLogServiceImpl implements UserLogService {

    private Config config = new NormalConfig();

    public UserLogServiceImpl(){

    }

    @Override
    public UserLoginRspVO login(UserLoginReqVO userLoginReqVO) {
        UserLoginRspVO userLoginRspVO = checkLogData(userLoginReqVO);
        if(userLoginRspVO.getLogToken() != null){

        }
        return userLoginRspVO;
    }

    @Override
    public BaseRspVO logout(UserLogoutReqVO userLogoutReqVO) {

        return null;
    }

    private UserLoginRspVO checkLogData(UserLoginReqVO userLoginReqVO){
        UserQueryPasswordReqVO userQueryPasswordReqVO = new UserQueryPasswordReqVO();
        SimpleUtils.copyProperties(userLoginReqVO,userQueryPasswordReqVO);
        UserQueryPasswordRspVO userQueryPasswordRspVO = ServiceCenter.userDataService.getPassword(userQueryPasswordReqVO);
        UserLoginRspVO userLoginRspVO = new UserLoginRspVO();
        if(userLoginReqVO.getUserPassword().equals(userQueryPasswordRspVO.getUserPassword())){
            try {
                userLoginRspVO.setLogToken(new SnowFlake().generateGuid("2"));
                userLoginRspVO.setCode("000000");
                userLoginRspVO.setMsg("成功");
                UserQueryUserIdReqVO userQueryUserIdReqVO = new UserQueryUserIdReqVO();
                userQueryUserIdReqVO.setUserName(userLoginReqVO.getUserName());
                userQueryUserIdReqVO.setUserPassword(userQueryPasswordRspVO.getUserPassword());
                UserQueryUserIdRspVO userQueryUserIdRspVO = ServiceCenter.userDataService.getUserId(userQueryUserIdReqVO);
                UserOnlineReqVO userOnlineReqVO = new UserOnlineReqVO();
                userOnlineReqVO.setUserId(userQueryUserIdRspVO.getUserId());
                userOnlineReqVO.setLogToken(userLoginRspVO.getLogToken());
                userOnlineReqVO.setOnline("1");
                userOnlineReqVO.setLogIn(LocalDateTime.now().toString());
                ServiceCenter.mapper.setTableName(UserLogServiceImpl.class);
                ServiceCenter.mapper.save(userOnlineReqVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            userLoginRspVO.setCode("999999");
            userLoginRspVO.setMsg("失败");
        }
        return userLoginRspVO;
    }
}
