package app.game.vo;


import app.net.annotation.NotNull;
import lombok.Data;

@Data
public class UserLogoutReqVO {


    @NotNull
    private String logToken;

    @NotNull
    private String logInTime;

}
