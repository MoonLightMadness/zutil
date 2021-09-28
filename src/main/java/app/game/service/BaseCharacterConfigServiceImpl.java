package app.game.service;

import app.game.BaseCharacterConfigService;
import app.game.cons.ServiceCenter;
import app.game.dto.CharacterConfigDTO;
import app.mapper.annotation.TableName;

@TableName("character_config")
public class BaseCharacterConfigServiceImpl implements BaseCharacterConfigService {


    @Override
    public CharacterConfigDTO getCharacterConfigById(String characterId) {
        CharacterConfigDTO characterConfigDTO = new CharacterConfigDTO();
        characterConfigDTO.setCharacterId(characterId);
        ServiceCenter.mapper.setTableName(this.getClass());
        characterConfigDTO = (CharacterConfigDTO) ServiceCenter.mapper.selectOne(new CharacterConfigDTO(),characterConfigDTO);
        return characterConfigDTO;
    }
}
