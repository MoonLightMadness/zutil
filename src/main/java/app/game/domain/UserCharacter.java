package app.game.domain;

import lombok.Data;

@Data
public class UserCharacter {

    private String userId;

    private String characterId;

    private String createTime;

    private String updateTime;

    private String deleteFlag;


}
