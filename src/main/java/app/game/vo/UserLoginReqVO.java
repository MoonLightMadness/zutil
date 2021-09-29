package app.game.vo;

import app.net.annotation.NotNull;
import lombok.Data;

@Data
public class UserLoginReqVO {

    @NotNull
    private String userName;

    @NotNull
    private String userPassword;

}
