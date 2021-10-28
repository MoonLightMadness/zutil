package app.play;

import app.config.annotation.ConfigPath;
import app.config.impl.ConfigInitializer;
import app.game.vo.BaseRspVO;
import app.log.Log;
import app.net.NioServerSelector;
import app.net.base.TypeResponse;
import app.net.constants.TypeResponseConstant;
import app.reflect.BeanCenter;
import app.reflect.annotation.Fill;
import app.reflect.annotation.Path;
import app.system.ConfigCenter;
import app.system.Core;
import app.utils.SimpleUtils;

import java.util.Arrays;

/**
 * @ClassName : app.play.Application
 * @Description :
 * @Date 2021-09-18 13:32:38
 * @Author ZhangHL
 */
@Path("/main")
@Fill
public class Application {
    public static void main(String[] args) {
        Log log = Core.log;
        log.info("Application Start...");
        long start = System.currentTimeMillis();
        //Bean加载
        BeanCenter beanCenter = new BeanCenter();
        beanCenter.load();
        //开启服务
        NioServerSelector selector = new NioServerSelector();
        //selector.accept();
        selector.read();
        log.info("Application Start Success...\nCost:{}ms", System.currentTimeMillis() - start);
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Path("/page")
    public TypeResponse getMainPage(BaseRspVO baseRspVO){
        String body = new String(SimpleUtils.readFile(ConfigCenter.get("main.page")));
        body = body.replace("\r","");
        body = body.replace("\n","");
        TypeResponse typeResponse = new TypeResponse();
        typeResponse.setType(TypeResponseConstant.HTML_OK);
        typeResponse.setData(body);
        return typeResponse;

    }
}
