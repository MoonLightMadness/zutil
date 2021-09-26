package app.play;

import app.net.NioServerSelector;
import app.net.WorkTrigger;
import app.reflect.ReflectUtils;
import app.reflect.annotation.Path;
import app.reflect.container.Indicators;

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

    @Path("/test8")
    public void test(testVO testVO){
        System.out.println("RT Run");
        System.out.println(testVO.getTest());

    }


}
