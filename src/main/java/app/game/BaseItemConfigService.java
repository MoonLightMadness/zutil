package app.game;

import app.game.domain.ItemConfig;

public interface BaseItemConfigService {

    ItemConfig getItemConfigByItemId(String itemId);

}
