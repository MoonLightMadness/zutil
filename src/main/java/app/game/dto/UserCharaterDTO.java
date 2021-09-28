package app.game.dto;

import lombok.Data;

@Data
public class UserCharaterDTO {

    private String userId;

    private String characterId;

    private String createTime;

    private String updateTime;

    private String deleteFlag;

}
