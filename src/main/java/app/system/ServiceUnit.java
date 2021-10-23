package app.system;

import app.game.cons.ServiceCenter;
import app.mapper.impl.Mapper;

/**
 * @ClassName : app.system.ServiceUnit
 * @Description :
 * @Date 2021-10-22 15:42:35
 * @Author ZhangHL
 */
public class ServiceUnit {

    public Mapper mapper = ServiceCenter.mapper;

    static {
        ConfigCenter.load();
    }

}
