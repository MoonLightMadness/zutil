package app.game.service;

import app.game.BaseUserLogDataService;
import app.game.cons.ServiceCenter;
import app.game.domain.UserLogData;
import app.game.vo.BaseUserDataQueryReqVO;
import app.game.vo.BaseUserLogDataQueryReqVO;
import app.mapper.annotation.TableName;
import app.utils.Packer;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName : app.game.service.BaseUserLogDataServiceImpl
 * @Description :
 * @Date 2021-09-27 08:39:25
 * @Author ZhangHL
 */
@TableName("user_log_data")
public class BaseUserLogDataServiceImpl implements BaseUserLogDataService {

    @Override
    public List<UserLogData> getUserLogDataByUserId(String userId) {
        BaseUserLogDataQueryReqVO baseUserLogDataQueryReqVO = new BaseUserLogDataQueryReqVO();
        baseUserLogDataQueryReqVO.setUserId(userId);
        ServiceCenter.mapper.setTableName(this.getClass());
        Object[] userLogDatas = ServiceCenter.mapper.selectList(new UserLogData(),baseUserLogDataQueryReqVO);
        return Packer.pack(userLogDatas);
    }
}
