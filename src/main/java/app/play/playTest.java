package app.play;

import app.config.annotation.ConfigPath;
import app.config.annotation.ConfigValue;
import app.config.impl.ConfigInitializer;
import app.config.impl.NormalConfig;
import app.config.utils.ConfigUtils;
import app.db.SqlBuilder;
import app.db.impl.SqlBuilderImpl;
import app.game.ProgressBar;
import app.game.domain.CharacterConfig;
import app.game.domain.CharacterData;
import app.game.domain.UserLogData;
import app.game.service.BaseCharacterConfigServiceImpl;
import app.game.service.BaseUserOnlineServiceImpl;
import app.game.service.UserBagServiceImpl;
import app.game.vo.GetItemReqVO;
import app.log.LogFactory;
import app.mapper.annotation.TableName;
import app.net.NioServerSelector;
import app.net.WorkTrigger;
import app.parser.JSONTool;
import app.reflect.BeanCenter;
import app.reflect.ReflectUtils;
import app.reflect.annotation.Fill;
import app.reflect.annotation.Path;
import app.reflect.container.Indicators;
import app.system.ConfigCenter;
import app.system.ServiceUnit;
import app.utils.MathUtils;
import app.utils.Packer;
import app.utils.SimpleUtils;
import app.utils.ThreadUitls;
import jdk.internal.dynalink.linker.LinkerServices;
import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Fill
@ConfigPath(value = {"${sys.path}"})
@Path("/playTest")
@TableName("character_config")
public class playTest extends ServiceUnit {


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
            LogFactory.getNormalLog().info("{}{}??????{}??????{}", i, "1", "2", "b");
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
        System.out.println(MathUtils.ExpressionInfix2Suffix(exp));
    }

    @Test
    public void test10() {
        NioServerSelector selector = new NioServerSelector();
        //selector.accept();
        selector.read();
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test11() {
        String json = "{\n" +
                "    \"userId\":\"6847889141309378560\",\n" +
                "    \"data\":[\n" +
                "    {\n" +
                "        \"lastTime\": \"185206\",\n" +
                "        \"logInTime\": \"2021-09-27 00:08:10\",\n" +
                "        \"logOffTime\": \"2021-09-27 00:11:15\",\n" +
                "        \"userId\": \"6847889141309378560\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"lastTime\": \"135774\",\n" +
                "        \"logInTime\": \"2021-09-27 08:14:26\",\n" +
                "        \"logOffTime\": \"2021-09-27 08:16:42\",\n" +
                "        \"userId\": \"6847889141309378560\"\n" +
                "    }\n" +
                "]\n" +
                "}";
        TestPojo testPojo;
        testPojo = (TestPojo) JSONTool.getObject(json.getBytes(StandardCharsets.UTF_8), TestPojo.class);
        System.out.println(new String(JSONTool.toJson(testPojo)));

    }

    @Test
    public void test12() {
        TestPojo testPojo = new TestPojo();
        Class clazz = testPojo.getClass();
        try {
            Object obj = clazz.newInstance();
            Field[] fs = clazz.getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                System.out.println(f.getType().toString() + " " + f.getName());
                System.out.println(f.getGenericType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ConfigValue("${test.property}")
    public String testStr;

    @Test
    public void test13() {
        try {
            ConfigInitializer configInitializer = new ConfigInitializer();
            configInitializer.loadConfigPath(new String[]{"."});
            ConfigUtils.getValue(this, this.getClass().getField("testStr"));
            System.out.println(this.testStr);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void testSub1() {
        int num = Integer.parseInt(testStr);
        System.out.println(num * num);
    }

    @Test
    public void test14() {
        BeanCenter beanCenter = new BeanCenter();
        beanCenter.load();
        playTest playTest = (app.play.playTest) beanCenter.get("playTest");
        playTest.testSub1();
        System.out.println(playTest.testStr);
    }

    @Test
    public void test15() {
        BeanCenter beanCenter = new BeanCenter();
        beanCenter.load();
        UserBagServiceImpl userBagService = (UserBagServiceImpl) beanCenter.get("UserBagServiceImpl");
        System.out.println(userBagService.baseUserOnlineService.getClass().getSimpleName());
    }

    @Test
    public void test16() {
        BeanCenter beanCenter = new BeanCenter();
        beanCenter.load();
        UserBagServiceImpl userBagService = Packer.pack(beanCenter.get("UserBagServiceImpl"));
        System.out.println(userBagService.baseUserOnlineService.getClass().getSimpleName());
    }

    @Test
    public void test17() {
        SqlBuilder sqlBuilder = new SqlBuilderImpl();
        sqlBuilder.setTable("act_sf_param_of").delete().where("task_handle_item", "123");
        sqlBuilder.and().where("prrr", "a");
        System.out.println(sqlBuilder);
    }

    @Test
    public void test18() {
        BeanCenter beanCenter = new BeanCenter();
        beanCenter.load();
        SimpleUtils.moveFile("C:\\Users\\Administrator\\Desktop\\temp\\save\\2.txt", "3.txt");
    }

    @Test
    public void test19() {
        System.out.println(SimpleUtils.StringFormatter("{}{}This{} {} a t{}t{}", "a", "b", "is", "es", "c"));
    }

    @Test
    public void test20(){
        CharacterConfig data = new CharacterConfig();
        data.setCharacterId("1");
        long start = System.currentTimeMillis();
        data = Packer.pack(mapper.selectOne(data,data));
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(data);
        data = new CharacterConfig();
        data.setCharacterId("2");
        start = System.currentTimeMillis();
        data = Packer.pack(mapper.selectOne(data,data));
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(data);
    }

    @Test
    public void test21(){
        //ConfigCenter.load();
//        BeanCenter beanCenter = new BeanCenter();
//        beanCenter.load();
        System.out.println(ConfigCenter.get("mysql.path"));
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
