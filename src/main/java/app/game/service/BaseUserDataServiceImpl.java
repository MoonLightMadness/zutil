package app.game.service;

import app.game.BaseUserDataService;
import app.game.cons.ServiceCenter;
import app.game.domain.UserData;
import app.game.vo.BaseUserDataQueryReqVO;
import app.mapper.annotation.TableName;

/**
 * @ClassName : app.game.service.BaseUserDataServiceImpl
 * @Description :
 * @Date 2021-09-27 08:22:54
 * @Author ZhangHL
 */
@TableName("user_config")
public class BaseUserDataServiceImpl implements BaseUserDataService {
    @Override
    public UserData getUserDataById(String userId) {
        BaseUserDataQueryReqVO baseUserDataQueryReqVO = new BaseUserDataQueryReqVO();
        baseUserDataQueryReqVO.setUserId(userId);
        ServiceCenter.mapper.setTableName(this.getClass());
        UserData userData = (UserData) ServiceCenter.mapper.selectOne(new UserData(),baseUserDataQueryReqVO);
        return userData;
    }

    @Override
    public UserData getUserDataByUserLogInfo(String userName, String userPassword) {
        BaseUserDataQueryReqVO baseUserDataQueryReqVO = new BaseUserDataQueryReqVO();
        baseUserDataQueryReqVO.setUserName(userName);
        baseUserDataQueryReqVO.setUserPassword(userPassword);
        ServiceCenter.mapper.setTableName(this.getClass());
        UserData userData = (UserData) ServiceCenter.mapper.selectOne(new UserData(),baseUserDataQueryReqVO);
        return userData;
    }
}
