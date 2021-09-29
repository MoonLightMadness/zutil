package app.game.vo;

import app.net.annotation.NotNull;
import lombok.Data;

@Data
public class UserRegisterReqVO {

    @NotNull
    private String userName;

    @NotNull
    private String userPassword;


}
