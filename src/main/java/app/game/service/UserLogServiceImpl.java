package app.game.service;

import app.game.UserLogService;
import app.game.cons.ServiceCenter;
import app.game.vo.UserLoginReqVO;
import app.game.vo.UserLoginRspVO;
import app.game.vo.UserQueryPasswordReqVO;
import app.game.vo.UserQueryPasswordRspVO;
import app.mapper.annotation.TableName;
import app.utils.SimpleUtils;
import app.utils.guid.impl.SnowFlake;

@TableName("user_online")
public class UserLogServiceImpl implements UserLogService {

    @Override
    public UserLoginRspVO login(UserLoginReqVO userLoginReqVO) {
        UserQueryPasswordReqVO userQueryPasswordReqVO = new UserQueryPasswordReqVO();
        SimpleUtils.copyProperties(userLoginReqVO,userQueryPasswordReqVO);
        UserQueryPasswordRspVO userQueryPasswordRspVO = ServiceCenter.userDataService.getPassword(userQueryPasswordReqVO);
        UserLoginRspVO userLoginRspVO = new UserLoginRspVO();
        if(userLoginReqVO.getUserPassword().equals(userQueryPasswordRspVO.getUserPassword())){
            try {
                userLoginRspVO.setLogToken(new SnowFlake().generateGuid("2"));
                userLoginRspVO.setCode("000000");
                userLoginRspVO.setMsg("成功");
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
