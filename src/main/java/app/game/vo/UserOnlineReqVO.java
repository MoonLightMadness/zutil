package app.game.vo;

import lombok.Data;

@Data
public class UserOnlineReqVO {

    private String userId;

    private String logToken;

    private String online;

    private String logIn;

}
