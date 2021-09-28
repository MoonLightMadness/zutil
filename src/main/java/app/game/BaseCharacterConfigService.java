package app.game;

import app.game.dto.CharacterConfigDTO;

import java.util.List;

public interface BaseCharacterConfigService {


    CharacterConfigDTO getCharacterConfigById(String characterId);


    List<CharacterConfigDTO> getAllConfig();
}
