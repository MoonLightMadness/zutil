package app.game.vo;

import app.net.annotation.NotNull;
import lombok.Data;

/**
 * @ClassName : app.game.vo.GetItemReqVO
 * @Description :
 * @Date 2021-09-29 15:40:11
 * @Author ZhangHL
 */
@Data
public class GetItemReqVO {

    @NotNull
    private String itemId;

    @NotNull
    private String userId;

}
