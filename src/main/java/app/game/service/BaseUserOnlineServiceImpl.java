package app.game.service;

import app.game.BaseUserOnlineService;
import app.game.cons.ServiceCenter;
import app.game.domain.UserOnline;
import app.game.vo.BaseUserOnlineQueryReqVO;
import app.mapper.annotation.TableName;

/**
 * @ClassName : app.game.service.BaseUserOnlineServiceImpl
 * @Description :
 * @Date 2021-09-27 08:32:59
 * @Author ZhangHL
 */
@TableName("user_online")
public class BaseUserOnlineServiceImpl implements BaseUserOnlineService {
    @Override
    public UserOnline getUserOnlieByUserId(String userId) {
        BaseUserOnlineQueryReqVO baseUserOnlineQueryReqVO = new BaseUserOnlineQueryReqVO();
        baseUserOnlineQueryReqVO.setUserId(userId);
        UserOnline userOnline = (UserOnline) ServiceCenter.mapper.selectOne(new UserOnline(),baseUserOnlineQueryReqVO);
        return userOnline;
    }

    @Override
    public UserOnline getUserOnlineByLogToken(String logToken) {
        BaseUserOnlineQueryReqVO baseUserOnlineQueryReqVO = new BaseUserOnlineQueryReqVO();
        baseUserOnlineQueryReqVO.setLogToken(logToken);
        UserOnline userOnline = (UserOnline) ServiceCenter.mapper.selectOne(new UserOnline(),baseUserOnlineQueryReqVO);
        return userOnline;
    }
}
