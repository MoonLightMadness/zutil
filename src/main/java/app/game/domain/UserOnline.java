package app.game.domain;

import lombok.Data;

@Data
public class UserOnline {

    private String userId;

    private String online;

    private String logInTime;

    private String logToken;

}
