package app.game.service;

import app.game.UserLogDataService;
import app.game.vo.UserLoginDataReqVO;
import app.mapper.annotation.TableName;

@TableName("user_log_data")
public class UserLogDataServiceImpl implements UserLogDataService {
    @Override
    public void updateLogData(UserLoginDataReqVO userLoginDataReqVO) {

    }
}
