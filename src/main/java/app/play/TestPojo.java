package app.play;

import app.game.domain.UserLogData;
import lombok.Data;

import java.util.List;

/**
 * @ClassName : app.play.TestPojo
 * @Description :
 * @Date 2021-09-29 10:35:26
 * @Author ZhangHL
 */
@Data
public class TestPojo {

    private String userId;

    private List<UserLogData> data;
}
