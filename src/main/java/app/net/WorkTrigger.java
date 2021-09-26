package app.net;

import app.http.HttpParser;
import app.http.entity.HttpRequestEntity;
import app.http.entity.HttpRespondEntity;
import app.log.Log;
import app.net.entity.Message;
import app.parser.JSONTool;
import app.reflect.container.Indicators;
import app.reflect.domain.ReflectIndicator;
import app.system.Core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName : app.net.WorkTrigger
 * @Description :
 * @Date 2021-09-26 13:48:03
 * @Author ZhangHL
 */
public class WorkTrigger implements Runnable{

    private NioMessageQueue messageQueue;

    private Indicators indicators;

    private Log log = Core.log;

    public WorkTrigger(NioMessageQueue nioMessageQueue,Indicators indicators){
        this.messageQueue = nioMessageQueue;
        this.indicators = indicators;
    }



    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true){
            if(messageQueue.hasElement()){
                Message message = messageQueue.get();
                invoke(message);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void invoke(Message message){
        try {
            long start = System.currentTimeMillis();
            HttpRequestEntity httpRequestEntity = parseData(message.getData());
            ReflectIndicator reflectIndicator = indicators.get(httpRequestEntity.getArgs());
            Class clazz = Class.forName(reflectIndicator.getClassPath());
            Method method = getMetod(clazz,reflectIndicator.getMethodName());
            Object obj = JSONTool.getObject(httpRequestEntity.getBody().getBytes(StandardCharsets.UTF_8)
                    ,method.getParameterTypes()[0]);
            Object oc = clazz.newInstance();
            Object res = method.invoke(oc,obj);
            returnResult(res,message.getChannel());
            log.info("环节结束，用时:{}ms",System.currentTimeMillis()-start);
        } catch (ClassNotFoundException e) {
            log.error("未找到该类:{}",e);
            e.printStackTrace();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            log.error("发生错误,原因:{}",e);
            e.printStackTrace();
        }

    }

    private void returnResult(Object res, SocketChannel channel){
        if(res != null){
            HttpRespondEntity httpRespondEntity = new HttpRespondEntity();
            byte[] bres = JSONTool.toJson(res);
            httpRespondEntity.setBody(new String(bres));
            byte[] respond = httpRespondEntity.toString().getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(respond.length);
            buffer.put(respond);
            buffer.flip();
            try {
                channel.write(buffer);
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Method getMetod(Class clazz,String name){
        Method[] methods = clazz.getMethods();
        for (Method method : methods){
            if(method.getName().equals(name)){
                return method;
            }
        }
        return null;
    }

    private HttpRequestEntity parseData(byte[] data){
        return HttpParser.parseRequestEntiy(data);
    }


}