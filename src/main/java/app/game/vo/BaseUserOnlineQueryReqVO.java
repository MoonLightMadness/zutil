package app.game.vo;

import lombok.Data;

/**
 * @ClassName : app.game.vo.BaseUserOnlineQueryReqVO
 * @Description :
 * @Date 2021-09-27 08:33:30
 * @Author ZhangHL
 */
@Data
public class BaseUserOnlineQueryReqVO {
    private String userId;

    private String online;

    private String logInTime;

    private String logToken;
}
