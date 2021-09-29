package app.game.vo;

import app.game.domain.CharacterData;
import app.net.annotation.NotNull;
import lombok.Data;

@Data
public class UserCreateCharacterReqVO {

    private String userId;

    private String characterId;

    private String createTime;

    private String updateTime;

    private String deleteFlag;


    private String characterData;


}
