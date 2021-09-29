package app.net.entity;

import lombok.Data;

/**
 * @ClassName : app.net.entity.NullRspVO
 * @Description :
 * @Date 2021-09-29 10:07:27
 * @Author ZhangHL
 */
@Data
public class CheckRspVO {

    private String msg;

    private String code = "999999";
}
