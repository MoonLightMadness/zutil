package app.game.vo;

import lombok.Data;

/**
 * @ClassName : app.game.vo.BaseUserLogDataQueryReqVO
 * @Description :
 * @Date 2021-09-27 08:38:28
 * @Author ZhangHL
 */
@Data
public class BaseUserLogDataQueryReqVO {
    private String userId;

    private String logInTime;

    private String logOffTime;

    private String lastTime;
}
