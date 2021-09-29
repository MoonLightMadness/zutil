package app.game.vo;

import app.net.annotation.NotNull;
import lombok.Data;

/**
 * @ClassName : app.game.vo.BaseUserLogDataQueryReqVO
 * @Description :
 * @Date 2021-09-27 08:38:28
 * @Author ZhangHL
 */
@Data
public class BaseUserLogDataQueryReqVO {

    @NotNull
    private String userId;

    private String logInTime;

    private String logOffTime;

    private String lastTime;
}
