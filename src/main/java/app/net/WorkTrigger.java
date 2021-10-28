package app.net;

import app.game.vo.BaseExceptionRspVO;
import app.game.vo.BaseRspVO;
import app.http.HttpParser;
import app.http.entity.HttpRequestEntity;
import app.http.entity.HttpRespondEntity;
import app.http.entity.HttpRespondHTMLEntity;
import app.log.Log;
import app.net.annotation.NotNull;
import app.net.annotation.Valid;
import app.net.base.Response;
import app.net.base.ResponseWarpper;
import app.net.base.TypeResponse;
import app.net.entity.CheckRspVO;
import app.net.entity.Message;
import app.parser.JSONTool;
import app.parser.exception.ServiceException;
import app.reflect.BeanManager;
import app.reflect.container.Indicators;
import app.reflect.domain.ReflectIndicator;
import app.system.ConfigCenter;
import app.system.Core;
import app.utils.Packer;
import app.utils.SimpleUtils;
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
            } catch (Exception e) {
                Core.log.error("内部错误，原因:{}", e);
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
            log.info("进入invoke方法,Message:{}", new String(message.getData()));
            long start = System.currentTimeMillis();
            Response response = parseData(message.getData());
            if (response.getCode() == "999999") {
                returnFailed(message, response.getCode(), response.getMsg());
            }
            HttpRequestEntity httpRequestEntity = Packer.pack(response.getData());
            if (httpRequestEntity == null) {
                log.info("该消息为Respond,不予处理");
                return;
            }
            //获取文件
            if (httpRequestEntity.getArgs().indexOf('.') != -1) {
                returnResult(getFile(httpRequestEntity.getArgs()), message.getChannel());
                return;
            }
            ReflectIndicator reflectIndicator = indicators.get(httpRequestEntity.getArgs());
            if (reflectIndicator == null) {
                returnFailed(message, "999999", "路径错误");
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
                returnFailed(message, "999999", "空对象oc");
            }
            try {
                log.info("正在进入{}方法", method.getName());
                System.out.println(oc);
                Object res = method.invoke(oc, obj);
                returnResult(res, message.getChannel());
            } catch (Exception e) {
                String msg = e.getCause().getMessage();
                returnFailed(message, "999999", msg);
            }
            log.info("环节结束，用时:{}ms", System.currentTimeMillis() - start);
        } catch (ClassNotFoundException e) {
            log.error("未找到该类:{}", e);
            e.printStackTrace();
        }

    }

    private String getFile(String args) {
        args = args.substring(1);
        if(args.indexOf('/') == -1){
            return null;
        }
        String path = args.substring(0, args.indexOf('/'));
        int len = path.length() + 1;
        path = ConfigCenter.get("mapper." + path);
        path = path + args.substring(len);
        return new String(SimpleUtils.readFile(path));
    }

    private void returnResult(Object res, SocketChannel channel) {
        if (res != null) {
            if (res.getClass() == TypeResponse.class) {
                returnHtmlPage(res, channel);
                return;
            }
            HttpRespondEntity httpRespondEntity = new HttpRespondEntity();
            byte[] bres;
            if (res.getClass() != String.class) {
                bres = JSONTool.toJson(res);
            } else {
                bres = ((String) res).getBytes();
            }
            httpRespondEntity.setBody(new String(bres));
            log.info("返回响应报文:{}", httpRespondEntity);
            sendData(httpRespondEntity.toString().getBytes(StandardCharsets.UTF_8), channel);
        }
    }

    private void returnHtmlPage(Object res, SocketChannel channel) {
        TypeResponse response = Packer.pack(res);
        HttpRespondHTMLEntity httpRespondHTMLEntity = new HttpRespondHTMLEntity();
        httpRespondHTMLEntity.setBody(response.getData().toString());
        sendData(httpRespondHTMLEntity.toString().getBytes(StandardCharsets.UTF_8), channel);
    }

    private void sendData(byte[] data, SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        int len = data.length;
        buffer.put(data);
        buffer.flip();
        try {
            int sended = channel.write(buffer);
            len -= sended;
            if(len > 0){
                byte[] rest = new byte[len];
                System.arraycopy(data,sended,rest,0,len);
                sendData(rest,channel);
            }
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        HttpRespondEntity httpRespondEntity = new HttpRespondEntity();
        httpRespondEntity.setRespondCode("500");
        httpRespondEntity.setBody(baseExceptionRspVO.toString());
        log.info("返回响应报文:{}", httpRespondEntity);
        sendData(httpRespondEntity.toString().getBytes(StandardCharsets.UTF_8), message.getChannel());
    }

}
