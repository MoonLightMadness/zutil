package app.play;

import app.config.annotation.ConfigPath;
import app.config.impl.ConfigInitializer;
import app.config.impl.NormalConfig;
import app.game.ProgressBar;
import app.log.LogFactory;
import app.net.NioServerSelector;
import app.net.WorkTrigger;
import app.reflect.ReflectUtils;
import app.reflect.annotation.Path;
import app.reflect.container.Indicators;
import app.utils.MathUtils;
import app.utils.ThreadUitls;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;


@ConfigPath(value = {"${sys.path}"})
@Path("/playTest")
public class playTest {


    @Test
    public void test1() {
        playTest playTest = new playTest();
        String tc1 = "This is a test";
        String tc3 = "{}This is{} a test{}";
        playTest.messageHandler(tc1);
        System.out.println(playTest.logBuilder.toString());
        playTest.logBuilder = new StringBuilder();
        playTest.messageHandler(tc3, "1", "2", "3");
        System.out.println(playTest.logBuilder.toString());
        playTest.logBuilder = new StringBuilder();
        playTest.messageHandler(tc3);
        System.out.println(playTest.logBuilder.toString());
    }

    @Test
    public void test2() {
        NormalConfig normalConfig = new NormalConfig();
        normalConfig.update("test.property", "2", normalConfig.read("sys.path"));
        System.out.println(normalConfig.read("test.property"));
        normalConfig.refresh();
        System.out.println(normalConfig.read("test.property"));
    }

    @Test
    public void test3() throws IOException {
        File file = new File("./config/meta.txt");
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getParent());
    }

    @Test
    public void test4() throws IOException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            LogFactory.getNormalLog().info("{}{}这是{}测试{}", i, "1", "2", "b");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public StringBuilder logBuilder = new StringBuilder();

    public void messageHandler(String msg, Object... args) {
        for (int i = 0; i < args.length; i++) {
            int pointer = msg.indexOf('{');
            if (pointer != -1) {
                logBuilder.append(msg, 0, pointer);
                if (args[i] instanceof Exception) {
                    logBuilder.append(getStackTrace((Exception) args[i]));
                } else {
                    logBuilder.append(args[i].toString());
                }
                msg = msg.substring(pointer + 2);
            }
        }
        logBuilder.append(msg);
    }

    @Test
    public void test5() {
        ConfigInitializer configInitializer = new ConfigInitializer();
        configInitializer.loadConfigPath(new String[]{"."});
        ConfigPath configPath = playTest.class.getAnnotation(ConfigPath.class);
        System.out.println(Arrays.toString(configPath.value()));
    }

    @Test
    public void test6() {
        Method[] methods = ConfigPath.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName() + " " + Arrays.toString(method.getParameterTypes()));
        }
    }

    @Test
    public void test7() {
        Indicators indicators = new Indicators();
        indicators.initialize();
        ReflectUtils.constructReflectIndicator("app", indicators);

        NioServerSelector selector = new NioServerSelector();
        //selector.accept();
        selector.read();

        try {
            SocketChannel channel = SocketChannel.open();
            //channel.configureBlocking(false);
            System.out.println(channel.connect(new InetSocketAddress("127.0.0.1", 10010)));
            Thread.sleep(1000);
            String data = "POST /Test/test8 HTTP/1.1\n" +
                    "Content-Type: application/json\n" +
                    "User-Agent: PostmanRuntime/7.28.1\n" +
                    "Accept: */*\n" +
                    "Postman-Token: 79577fc3-5568-4d1b-9afd-e4d7cb7a46e2\n" +
                    "Host: 127.0.0.1:9004\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Connection: keep-alive\n" +
                    "Content-Length: 36\n" +
                    "\n" +
                    "{\n" +
                    "\n" +
                    "\n \"test\":\" TTT \"\n" +
                    "}";
            byte[] bdata = data.getBytes(StandardCharsets.UTF_8);
//            for (int i = 0; i < 10; i++) {
//                System.out.println(i);
//                ByteBuffer buffer = ByteBuffer.allocate(bdata.length);
//                buffer.put(bdata);
//                buffer.flip();
//                channel.write(buffer);
//                Thread.sleep(100);
//            }
            WorkTrigger workTrigger1 = new WorkTrigger(selector.getQueue(), indicators);
            WorkTrigger workTrigger2 = new WorkTrigger(selector.getQueue(), indicators);
            WorkTrigger workTrigger3 = new WorkTrigger(selector.getQueue(), indicators);
            ThreadUitls.submit(workTrigger1);
            ThreadUitls.submit(workTrigger2);
            ThreadUitls.submit(workTrigger3);
            Thread.sleep(1000);

            Thread.sleep(50000);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Path("/test8")
    public void test8() {
        Indicators indicators = new Indicators();
        indicators.initialize();
        ReflectUtils.constructReflectIndicator(".", indicators);
        System.out.println(indicators.get("/playTest/test8"));
    }

    @Test
    public void test9() {
        String exp = "5 + (18 + 3) * 5 - 2";
        System.out.println(MathUtils.calculate(exp));
    }

    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

}
