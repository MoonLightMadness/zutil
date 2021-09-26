package app.game.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginRspVO extends BaseRspVO {

    private String logToken;

}
