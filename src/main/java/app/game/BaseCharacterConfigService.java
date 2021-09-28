package app.game;

import app.game.dto.CharacterConfigDTO;

public interface BaseCharacterConfigService {


    CharacterConfigDTO getCharacterConfigById(String characterId);

}
