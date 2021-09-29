package app.game.web.frontend;

import app.game.cons.ServiceCenter;
import app.game.domain.ItemConfig;
import app.game.vo.BaseRspVO;
import app.game.vo.GetItemReqVO;
import app.reflect.annotation.Path;

/**
 * @ClassName : app.game.web.frontend.UserBagDataController
 * @Description :
 * @Date 2021-09-29 15:38:58
 * @Author ZhangHL
 */
@Path("/data/bag")
public class UserBagDataController {

    @Path("/getitem")
    public BaseRspVO getItem(GetItemReqVO getItemReqVO){
        ItemConfig itemConfig = new ItemConfig();
        itemConfig.setItemId(getItemReqVO.getItemId());
        ServiceCenter.userBagService.getItem(itemConfig,getItemReqVO.getUserId());
        return new BaseRspVO();
    }


}
