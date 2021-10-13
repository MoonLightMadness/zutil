package app.game.service;

import app.game.BaseUserOnlineService;
import app.game.UserBagService;
import app.game.cons.ServiceCenter;
import app.game.domain.*;
import app.game.exception.UserException;
import app.game.exception.UserExceptionEnum;
import app.game.vo.BaseRspVO;
import app.game.vo.GetItemReqVO;
import app.mapper.annotation.TableName;
import app.parser.JSONTool;
import app.reflect.annotation.AutoFill;
import app.reflect.annotation.Fill;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName : app.game.service.UserBagServiceImpl
 * @Description :
 * @Date 2021-09-29 15:18:54
 * @Author ZhangHL
 */
@Fill
@TableName("user_bag_data")
public class UserBagServiceImpl implements UserBagService {

    @AutoFill
    public BaseUserOnlineService baseUserOnlineService;


    @Override
    public UserBagItemData getUserBagItemDataByUserId(String userId) {
        UserBagMetaData userBagMetaData = new UserBagMetaData();
        userBagMetaData.setUserId(userId);
        ServiceCenter.mapper.setTableName(this.getClass());
        userBagMetaData = (UserBagMetaData) ServiceCenter.mapper.selectOne(new UserBagMetaData(),userBagMetaData);
        UserBagItemData userBagItemData;
        userBagItemData = (UserBagItemData) JSONTool.getObject(userBagMetaData.getBagData().getBytes(StandardCharsets.UTF_8)
        ,UserBagItemData.class);
        return userBagItemData;
    }

    @SneakyThrows
    @Override
    public BaseRspVO getItem(ItemConfig itemConfig, GetItemReqVO getItemReqVO) {
        BaseRspVO baseRspVO = new BaseRspVO();
        itemConfig = ServiceCenter.baseItemConfigService.getItemConfigByItemId(itemConfig.getItemId());
        boolean isExist = false;
        UserOnline userOnline = ServiceCenter.baseUserOnlineService.getUserOnlineByLogToken(getItemReqVO.getLogToken());
        if(userOnline == null){
            throw new UserException(UserExceptionEnum.UEE_001.getId(),UserExceptionEnum.UEE_001.getMsg());
        }
        UserBagItemData userBagItemData = getUserBagItemDataByUserId(userOnline.getUserId());
        List<UserItem> items = userBagItemData.getBagData();
        for(UserItem item : items){
            if(item.getNum()!=null && item.getUserBagItemData().getItemId().equals(itemConfig.getItemId())){
                item.setNum(String.valueOf(Integer.parseInt(item.getNum())+1));
                isExist = true;
                break;
            }
        }
        if(!isExist){
            UserItem userItem = new UserItem();
            userItem.setNum("1");
            userItem.setUserBagItemData(itemConfig);
            items.add(userItem);
        }
        saveUserBagItemData(userBagItemData);
        return baseRspVO;
    }

    @Override
    public void saveUserBagItemData(UserBagItemData userBagItemData) {
        ServiceCenter.mapper.setTableName(this.getClass());
        UserBagMetaData userBagMetaData = new UserBagMetaData();
        userBagMetaData.setUserId(userBagItemData.getUserId());
        userBagMetaData.setBagData(JSON.toJSONString(userBagItemData));
        UserBagItemData condition = new UserBagItemData();
        condition.setUserId(userBagItemData.getUserId());
        ServiceCenter.mapper.update(userBagMetaData,condition);
    }


}
