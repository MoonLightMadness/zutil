package app.game.vo;

import lombok.Data;

/**
 * @ClassName : app.game.vo.BaseUserDataQueryReqVO
 * @Description :
 * @Date 2021-09-27 08:27:42
 * @Author ZhangHL
 */
@Data
public class BaseUserDataQueryReqVO {

    private String id;

    private String userId;

    private String userName;

    private String userPassword;

    private String timestamp;


}
