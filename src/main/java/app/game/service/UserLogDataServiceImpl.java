package app.game.service;

import app.game.UserLogDataService;
import app.game.cons.ServiceCenter;
import app.game.domain.UserLogData;
import app.game.domain.UserOnline;
import app.game.vo.*;
import app.mapper.annotation.TableName;
import app.utils.SimpleUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@TableName("user_log_data")
public class UserLogDataServiceImpl implements UserLogDataService {
    /**
     * 登记登录时间
     * @param userLoginDataReqVO
     * @return
     * @author zhl
     * @date 2021-09-26 23:20
     * @version V1.0
     */
    @Override
    public void updateLoginData(UserLoginDataReqVO userLoginDataReqVO) {
        ServiceCenter.mapper.setTableName(UserLogDataServiceImpl.class);
        ServiceCenter.mapper.save(userLoginDataReqVO);
    }

    /**
     * 登记登出时间
     * @param
     * @return
     * @author zhl
     * @date 2021-09-26 23:37
     * @version V1.0
     */
    @Override
    public void updateLogoffData(UserOfflineReqVO userOfflineReqVO) {
        LocalDateTime offTime = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.parse(userOfflineReqVO.getLogInTime().replace(" ","T"));
        String last = String.valueOf(startTime.until(offTime, ChronoUnit.MILLIS));
        UserLogData userLogData = new UserLogData();
        userLogData.setLastTime(last);
        userLogData.setLogOffTime(offTime.toString());
        UserLogData condition = new UserLogData();
        condition.setLogInTime(userOfflineReqVO.getLogInTime());
        QueryUserOnlineReqVO queryUserOnlineReqVO = new QueryUserOnlineReqVO();
        SimpleUtils.copyProperties(userOfflineReqVO,queryUserOnlineReqVO);
        UserOnline userOnline = ServiceCenter.userLogService.getUserOnlineByTokenAndInTime(queryUserOnlineReqVO);
        condition.setUserId(userOnline.getUserId());
        ServiceCenter.mapper.setTableName(UserLogDataServiceImpl.class);
        ServiceCenter.mapper.update(userLogData,condition);
    }
}
