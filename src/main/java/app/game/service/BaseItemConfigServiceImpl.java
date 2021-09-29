package app.game.service;

import app.game.BaseItemConfigService;
import app.game.cons.ServiceCenter;
import app.game.domain.ItemConfig;
import app.mapper.annotation.TableName;

/**
 * @ClassName : app.game.service.BaseItemConfigServiceImpl
 * @Description :
 * @Date 2021-09-29 15:44:59
 * @Author ZhangHL
 */
@TableName("item_config")
public class BaseItemConfigServiceImpl implements BaseItemConfigService {


    @Override
    public ItemConfig getItemConfigByItemId(String itemId) {
        ServiceCenter.mapper.setTableName(this.getClass());
        ItemConfig itemConfig = new ItemConfig();
        itemConfig.setItemId(itemId);
        itemConfig = (ItemConfig) ServiceCenter.mapper.selectOne(new ItemConfig(),itemConfig);
        return itemConfig;
    }
}
