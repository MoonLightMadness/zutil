package app.game.domain;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : app.game.domain.UserBagItemData
 * @Description :
 * @Date 2021-09-29 15:04:14
 * @Author ZhangHL
 */
@Data
public class UserBagItemData {

    private String userId;

    private List<UserItem> bagData;

}
