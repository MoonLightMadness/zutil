package app.game.service;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.game.UserLogService;
import app.game.cons.ServiceCenter;
import app.game.domain.UserOnline;
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
        ServiceCenter.mapper.setTableName(this.getClass());
        UserOnline userOnline = (UserOnline) ServiceCenter.mapper.selectOne(new UserOnline(),userLogoutReqVO);
        UserOfflineReqVO userOfflineReqVO = new UserOfflineReqVO();
        userOfflineReqVO.setLogToken(userOnline.getLogToken());
        userOfflineReqVO.setLogInTime(userOnline.getLogInTime());
        ServiceCenter.userLogDataService.updateLogoffData(userOfflineReqVO);
        BaseRspVO baseRspVO = new BaseRspVO();
        baseRspVO.setCode("000000");
        baseRspVO.setMsg("成功");
        ServiceCenter.mapper.setTableName(this.getClass());
        ServiceCenter.mapper.delete(userLogoutReqVO);
        return baseRspVO;
    }

    @Override
    public UserOnline getUserOnlineByTokenAndInTime(QueryUserOnlineReqVO queryUserOnlineReqVO) {
        ServiceCenter.mapper.setTableName(this.getClass());
        UserOnline userOnline = (UserOnline) ServiceCenter.mapper.selectOne(new UserOnline(),queryUserOnlineReqVO);
        return userOnline;
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
                userOnlineReqVO.setLogInTime(SimpleUtils.getTimeStamp());
                userLoginRspVO.setLogInTime(userOnlineReqVO.getLogInTime());
                ServiceCenter.mapper.setTableName(UserLogServiceImpl.class);
                ServiceCenter.mapper.save(userOnlineReqVO);
                UserLoginDataReqVO userLoginDataReqVO = new UserLoginDataReqVO();
                userLoginDataReqVO.setUserId(userQueryUserIdRspVO.getUserId());
                userLoginDataReqVO.setLogInTime(userOnlineReqVO.getLogInTime());
                ServiceCenter.userLogDataService.updateLoginData(userLoginDataReqVO);
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
