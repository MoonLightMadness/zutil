package app.game;

import app.game.dto.UserCharaterDTO;

import java.util.List;

public interface BaseUserCharacterService {

    List<UserCharaterDTO> getUserCharacterByUserId(String userId);

    UserCharaterDTO getUserCharacterByUserIdAndCharaterId(String userId,String characterId);

}
