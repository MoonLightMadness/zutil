package app.play;

import app.config.annotation.ConfigPath;
import app.config.impl.ConfigInitializer;
import app.net.NioServerSelector;
import app.reflect.BeanCenter;

import java.util.Arrays;

/**
 * @ClassName : app.play.Application
 * @Description :
 * @Date 2021-09-18 13:32:38
 * @Author ZhangHL
 */
public class Application {
    public static void main(String[] args) {
        //Bean加载
        BeanCenter beanCenter = new BeanCenter();
        beanCenter.load();
        //开启服务
        NioServerSelector selector = new NioServerSelector();
        //selector.accept();
        selector.read();
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
