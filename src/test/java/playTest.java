import app.config.impl.NormalConfig;
import org.junit.Test;

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
        String meta = "C:\\Users\\Administrator\\Desktop\\temp\\meta.txt";
        NormalConfig normalConfig = new NormalConfig();
        normalConfig.setMeta(meta);
        normalConfig.write("test2","zutil2",normalConfig.read("path"));
        System.out.println(normalConfig.read("test2"));
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

    private String getStackTrace(Exception e){
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for(StackTraceElement element : e.getStackTrace()){
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }

}
