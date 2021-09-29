package app.game.service;

import app.game.BaseUserCharacterService;
import app.game.cons.ServiceCenter;
import app.game.dto.CharacterConfigDTO;
import app.game.dto.UserCharaterDTO;
import app.game.vo.BaseUserCharacterQueryReqVO;
import app.mapper.annotation.TableName;
import app.utils.Packer;

import java.util.List;

@TableName("user_characters")
public class BaseUserCharacterServiceImpl implements BaseUserCharacterService {
    @Override
    public List<UserCharaterDTO> getUserCharacterByUserId(String userId) {
        ServiceCenter.mapper.setTableName(this.getClass());
        Packer<UserCharaterDTO> packer = new Packer<>();
        UserCharaterDTO userCharaterDTO = new UserCharaterDTO();
        userCharaterDTO.setUserId(userId);
        List<UserCharaterDTO> list = packer.pack(ServiceCenter.mapper.selectList(new CharacterConfigDTO(),userCharaterDTO));
        return list;
    }

    @Override
    public UserCharaterDTO getUserCharacterByUserIdAndCharaterId(String userId, String characterId) {
        ServiceCenter.mapper.setTableName(this.getClass());
        UserCharaterDTO userCharaterDTO = new UserCharaterDTO();
        userCharaterDTO.setUserId(userId);
        userCharaterDTO.setCharacterId(characterId);
        userCharaterDTO = (UserCharaterDTO) ServiceCenter.mapper.selectOne(new CharacterConfigDTO(),userCharaterDTO);
        return userCharaterDTO;
    }

    @Override
    public UserCharaterDTO getSingleUserCharacter(BaseUserCharacterQueryReqVO baseUserCharacterQueryReqVO) {
        return getUserCharacterByUserIdAndCharaterId(baseUserCharacterQueryReqVO.getUserId(),baseUserCharacterQueryReqVO.getCharacterId());
    }
}
