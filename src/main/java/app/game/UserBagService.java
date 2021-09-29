package app.game;

import app.game.domain.ItemConfig;
import app.game.domain.UserBagItemData;

public interface UserBagService {

    UserBagItemData getUserBagItemDataByUserId(String userId);

    void getItem(ItemConfig itemConfig,String userId);

    void saveUserBagItemData(UserBagItemData userBagItemData);


}
