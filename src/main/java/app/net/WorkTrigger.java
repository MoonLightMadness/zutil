package app.net;

import app.game.vo.BaseExceptionRspVO;
import app.game.vo.BaseRspVO;
import app.http.HttpParser;
import app.http.entity.HttpRequestEntity;
import app.http.entity.HttpRespondEntity;
import app.log.Log;
import app.net.annotation.NotNull;
import app.net.annotation.Valid;
import app.net.base.Response;
import app.net.base.ResponseWarpper;
import app.net.entity.CheckRspVO;
import app.net.entity.Message;
import app.parser.JSONTool;
import app.parser.exception.ServiceException;
import app.reflect.BeanManager;
import app.reflect.container.Indicators;
import app.reflect.domain.ReflectIndicator;
import app.system.Core;
import app.utils.Packer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Field;
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
public class WorkTrigger implements Runnable {

    private NioMessageQueue messageQueue;

    private Indicators indicators;

    private Log log = Core.log;

    public WorkTrigger(NioMessageQueue nioMessageQueue, Indicators indicators) {
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
        while (true) {
            try {
                if (messageQueue.hasElement()) {
                    Message message = messageQueue.get();
                    if (message != null) {
                        invoke(message);
                    }
                }
            }catch (Exception e){
                Core.log.error("内部错误，原因:{}",e);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    private void invoke(Message message) {
        try {
            log.info("进入invoke方法,Message:{}",new String(message.getData()));
            long start = System.currentTimeMillis();
            Response response = parseData(message.getData());
            if(response.getCode() == "999999"){
                returnFailed(message,response.getCode(),response.getMsg());
            }
            HttpRequestEntity httpRequestEntity = Packer.pack(response.getData());
            if (httpRequestEntity == null) {
                log.info("该消息为Respond,不予处理");
                return;
            }
            System.out.println(httpRequestEntity.getArgs());
            ReflectIndicator reflectIndicator = indicators.get(httpRequestEntity.getArgs());
            if(reflectIndicator == null){
                returnFailed(message,"999999","路径错误");
                return;
            }
            Class clazz = Class.forName(reflectIndicator.getClassPath());
            Method method = getMetod(clazz, reflectIndicator.getMethodName());
            log.info("正在解析数据");
            String body = httpRequestEntity.getBody();
            Object obj = JSONTool.getObject(body.getBytes(StandardCharsets.UTF_8)
                    , method.getParameterTypes()[0]);
            log.info("数据解析完毕");
            CheckRspVO checkRspVO = checkValid(obj);
            if (checkRspVO.getCode().equals("999999")) {
                returnResult(checkRspVO, message.getChannel());
                return;
            }
            Object oc = BeanManager.get(clazz.getSimpleName());
            if (oc == null) {
                log.info("空对象oc,直接返回失败");
                returnFailed(message,"999999","空对象oc");
            }
            try {
                log.info("正在进入{}方法", method.getName());
                System.out.println(oc);
                Object res = method.invoke(oc, obj);
                returnResult(res, message.getChannel());
            } catch (Exception e) {
                String msg = e.getCause().getMessage();
                returnFailed(message,"999999",msg);
            }
            log.info("环节结束，用时:{}ms", System.currentTimeMillis() - start);
        } catch (ClassNotFoundException e) {
            log.error("未找到该类:{}", e);
            e.printStackTrace();
        }

    }

    private void returnResult(Object res, SocketChannel channel) {
        if (res != null) {
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

    private Method getMetod(Class clazz, String name) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    private CheckRspVO checkValid(Object obj) {
        CheckRspVO checkRspVO = new CheckRspVO();
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(NotNull.class)) {
                    Object test = field.get(obj);
                    if (test == null) {
                        checkRspVO.setMsg(field.getName() + " 不能为空");
                        checkRspVO.setCode("999999");
                        return checkRspVO;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        checkRspVO.setCode("000000");
        return checkRspVO;
    }

    private Response parseData(byte[] data) {
        return HttpParser.parseRequestEntiy(data);
    }

    private void returnFailed(Message message, String code, String msg) {
        BaseExceptionRspVO baseExceptionRspVO = new BaseExceptionRspVO();
        baseExceptionRspVO.setCode(code);
        baseExceptionRspVO.setMsg(msg);
        returnResult(baseExceptionRspVO, message.getChannel());
    }

}
