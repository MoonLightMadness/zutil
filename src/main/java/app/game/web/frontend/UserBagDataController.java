package app.game.web.frontend;

import app.game.cons.ServiceCenter;
import app.game.domain.ItemConfig;
import app.game.vo.BaseRspVO;
import app.game.vo.GetItemReqVO;
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


}
