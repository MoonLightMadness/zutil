package app.game.domain;

import lombok.Data;

@Data
public class UserLogData {

    private String userId;

    private String logInTime;

    private String logOffTime;

    private String lastTime;

}
