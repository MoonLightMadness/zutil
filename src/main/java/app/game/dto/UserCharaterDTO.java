package app.game.dto;

import app.game.domain.CharacterData;
import app.net.annotation.NotNull;
import lombok.Data;

@Data
public class UserCharaterDTO {

    @NotNull
    private String userId;

    @NotNull
    private String characterId;

    private String createTime;

    private String updateTime;

    private String deleteFlag;

    @NotNull
    private CharacterData characterData;

}
