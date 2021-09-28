package app.game.service;

import app.game.BaseUserCharacterService;
import app.game.cons.ServiceCenter;
import app.game.dto.CharacterConfigDTO;
import app.game.dto.UserCharaterDTO;
import app.utils.Packer;

import java.util.List;

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
}
