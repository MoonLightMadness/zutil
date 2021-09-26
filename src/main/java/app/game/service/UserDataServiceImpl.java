package app.game.service;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.game.UserDataService;
import app.game.domain.UserData;
import app.game.vo.*;
import app.log.Log;
import app.mapper.annotation.TableName;
import app.mapper.impl.Mapper;
import app.system.Core;
import app.utils.SimpleUtils;
import app.utils.guid.impl.SnowFlake;

import java.time.LocalDateTime;

@TableName("user_config")
public class UserDataServiceImpl implements UserDataService {

    Mapper mapper = new Mapper();

    Config config = new NormalConfig();

    Log log = Core.log;

    public UserDataServiceImpl(){
        mapper.initialize(UserDataServiceImpl.class,config.read("mysql.path"));
    }

    @Override
    public UserRegisterRspVO register(UserRegisterReqVO userRegisterReqVO) {
        UserData userData = new UserData();
        SimpleUtils.copyProperties(userRegisterReqVO,userData);
        SnowFlake snowFlake = new SnowFlake();
        try {
            userData.setUserId(snowFlake.generateGuid("1"));
            userData.setId(userData.getUserId());
            userData.setTimestamp(LocalDateTime.now().toString());
        } catch (Exception e) {
            log.error("生成用户id错误,原因:{}",e);
            e.printStackTrace();
        }
        mapper.save(userData);
        UserRegisterRspVO userRegisterRspVO = new UserRegisterRspVO();
        SimpleUtils.copyProperties(userData,userRegisterRspVO);
        return userRegisterRspVO;
    }

    @Override
    public UserQueryPasswordRspVO getPassword(UserQueryPasswordReqVO userQueryPasswordReqVO){
        UserQueryPasswordRspVO userQueryPasswordRspVO = (UserQueryPasswordRspVO) mapper.selectOne(new UserQueryPasswordRspVO(),userQueryPasswordReqVO);
        return userQueryPasswordRspVO;
    }

    @Override
    public UserQueryUserIdRspVO getUserId(UserQueryUserIdReqVO userQueryUserIdReqVO) {
        UserQueryUserIdRspVO userQueryUserIdRspVO = (UserQueryUserIdRspVO) mapper.selectOne(new UserQueryUserIdRspVO(),userQueryUserIdReqVO);
        return userQueryUserIdRspVO;
    }
}
