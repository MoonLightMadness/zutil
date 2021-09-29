package app.game;

import app.game.domain.ItemConfig;
import app.game.domain.UserBagItemData;
import app.game.vo.BaseRspVO;
import app.game.vo.GetItemReqVO;

public interface UserBagService {

    UserBagItemData getUserBagItemDataByUserId(String userId);

    BaseRspVO getItem(ItemConfig itemConfig, GetItemReqVO getItemReqVO);

    void saveUserBagItemData(UserBagItemData userBagItemData);


}
