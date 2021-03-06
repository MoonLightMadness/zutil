package app.game.service;

import app.game.BaseUserOnlineService;
import app.game.cons.ServiceCenter;
import app.game.domain.UserOnline;
import app.game.vo.BaseUserOnlineQueryReqVO;
import app.mapper.annotation.TableName;
import app.reflect.annotation.Service;

/**
 * @ClassName : app.game.service.BaseUserOnlineServiceImpl
 * @Description :
 * @Date 2021-09-27 08:32:59
 * @Author ZhangHL
 */
@Service
@TableName("user_online")
public class BaseUserOnlineServiceImpl implements BaseUserOnlineService {
    @Override
    public UserOnline getUserOnlieByUserId(String userId) {
        BaseUserOnlineQueryReqVO baseUserOnlineQueryReqVO = new BaseUserOnlineQueryReqVO();
        baseUserOnlineQueryReqVO.setUserId(userId);
        ServiceCenter.mapper.setTableName(this.getClass());
        UserOnline userOnline = (UserOnline) ServiceCenter.mapper.selectOne(new UserOnline(),baseUserOnlineQueryReqVO);
        return userOnline;
    }

    @Override
    public UserOnline getUserOnlineByLogToken(String logToken) {
        BaseUserOnlineQueryReqVO baseUserOnlineQueryReqVO = new BaseUserOnlineQueryReqVO();
        baseUserOnlineQueryReqVO.setLogToken(logToken);
        ServiceCenter.mapper.setTableName(this.getClass());
        UserOnline userOnline = (UserOnline) ServiceCenter.mapper.selectOne(new UserOnline(),baseUserOnlineQueryReqVO);
        return userOnline;
    }

    @Override
    public boolean checkOnline(String logToken) {
        UserOnline userOnline = this.getUserOnlineByLogToken(logToken);
        if(userOnline.getUserId() != null){
            return true;
        }
        return false;
    }
}
