package app.play;

import app.log.Log;
import app.net.NioServerSelector;
import app.net.WorkTrigger;
import app.reflect.ReflectUtils;
import app.reflect.annotation.Path;
import app.reflect.container.Indicators;
import app.system.Core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : app.play.Test
 * @Description :
 * @Date 2021-09-26 14:27:01
 * @Author ZhangHL
 */
@Path("/Test")
public class RTest {

    private Log log = Core.log;

    @Path("/test8")
    public void test(testVO testVO){
        log.info("进入--[测试]接口,入参:{}",testVO);
        System.out.println("RT Run");
        System.out.println(testVO.getTest());
        log.info("[测试]接口--结束,出参:{}",testVO);
    }


}
