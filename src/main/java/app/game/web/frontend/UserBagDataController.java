package app.game.web.frontend;

import app.game.cons.ServiceCenter;
import app.game.domain.ItemConfig;
import app.game.domain.UserBagItemData;
import app.game.domain.UserOnline;
import app.game.vo.BaseRspVO;
import app.game.vo.GetItemReqVO;
import app.game.vo.QueryReqVO;
import app.log.Log;
import app.reflect.annotation.Path;
import app.system.Core;

/**
 * @ClassName : app.game.web.frontend.UserBagDataController
 * @Description :
 * @Date 2021-09-29 15:38:58
 * @Author ZhangHL
 */
@Path("/data/bag")
public class UserBagDataController {

    Log log = Core.log;

    @Path("/getitem")
    public BaseRspVO getItem(GetItemReqVO getItemReqVO){
        log.info("进入[获取道具]接口,入参:{}",getItemReqVO);
        ItemConfig itemConfig = new ItemConfig();
        itemConfig.setItemId(getItemReqVO.getItemId());
        ServiceCenter.userBagService.getItem(itemConfig,getItemReqVO);
        log.info("[获取道具]接口执行完毕");
        return new BaseRspVO();
    }

    @Path("/getBag")
    public UserBagItemData getBagData(QueryReqVO queryReqVO){
        log.info("进入[获取背包数据]接口,入参:{}",queryReqVO);
        UserOnline userOnline = ServiceCenter.baseUserOnlineService.getUserOnlineByLogToken(queryReqVO.getLogToken());
        UserBagItemData userBagItemData = new UserBagItemData();
        userBagItemData = ServiceCenter.userBagService.getUserBagItemDataByUserId(userOnline.getUserId());
        log.info("[获取背包数据]接口执行完毕");
        return userBagItemData;
    }


}
