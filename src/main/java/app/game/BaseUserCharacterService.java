package app.game;

import app.game.dto.UserCharaterDTO;
import app.game.vo.BaseUserCharacterQueryReqVO;

import java.util.List;

public interface BaseUserCharacterService {

    List<UserCharaterDTO> getUserCharacterByUserId(String userId);

    UserCharaterDTO getUserCharacterByUserIdAndCharaterId(String userId,String characterId);

    UserCharaterDTO getSingleUserCharacter(BaseUserCharacterQueryReqVO baseUserCharacterQueryReqVO);

}
