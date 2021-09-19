package app.play;

import app.config.annotation.ConfigPath;
import app.config.impl.ConfigInitializer;
import app.config.impl.NormalConfig;
import app.log.LogFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;


@ConfigPath(value = {"${sys.path}"})
public class playTest {


    @Test
    public void test1(){
        playTest playTest = new playTest();
        String tc1 = "This is a test";
        String tc3 = "{}This is{} a test{}";
        playTest.messageHandler(tc1);
        System.out.println(playTest.logBuilder.toString());
        playTest.logBuilder = new StringBuilder();
        playTest.messageHandler(tc3,"1","2","3");
        System.out.println(playTest.logBuilder.toString());
        playTest.logBuilder = new StringBuilder();
        playTest.messageHandler(tc3);
        System.out.println(playTest.logBuilder.toString());
    }

    @Test
    public void test2(){
        NormalConfig normalConfig = new NormalConfig();
        normalConfig.update("test.property","2",normalConfig.read("sys.path"));
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
        for (int i =0;i<1000;i++){
            LogFactory.getNormalLog().info("{}{}这是{}测试{}",i,"1","2","b");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    public StringBuilder logBuilder = new StringBuilder();

    public void messageHandler(String msg,Object... args){
        for (int i = 0; i < args.length; i++) {
            int pointer=msg.indexOf('{');
            if(pointer!=-1){
                logBuilder.append(msg, 0, pointer);
                if(args[i] instanceof Exception){
                    logBuilder.append(getStackTrace((Exception) args[i]));
                }else {
                    logBuilder.append(args[i].toString());
                }
                msg=msg.substring(pointer+2);
            }
        }
        logBuilder.append(msg);
    }

    @Test
    public void test5(){
        ConfigInitializer configInitializer = new ConfigInitializer();
        configInitializer.loadConfigPath(new String[]{"."});
        ConfigPath configPath = playTest.class.getAnnotation(ConfigPath.class);
        System.out.println(Arrays.toString(configPath.value()));
    }

    @Test
    public void test6(){
        Method[] methods = ConfigPath.class.getMethods();
        for (Method method:methods){
            System.out.println(method.getName()+" "+ Arrays.toString(method.getParameterTypes()));
        }
    }

    private String getStackTrace(Exception e){
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for(StackTraceElement element : e.getStackTrace()){
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

}
