package app.log.impl;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.config.impl.SystemConfig;
import app.http.HttpParser;
import app.http.entity.HttpRequestEntity;
import app.http.entity.HttpRespondEntity;
import app.log.Log;
import app.log.vo.ReceiveLogReqVO;
import app.net.NioSender;
import app.parser.JSONTool;
import lombok.SneakyThrows;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class NormalLog implements Log {

    private Config config = new NormalConfig();

    private volatile StringBuilder logBuilder = new StringBuilder();

    private String preSave;

    private SocketChannel socketChannel;

    private boolean remote = false;

    public NormalLog() {
        try {
            remote = Boolean.parseBoolean(config.read("logServer.enabled"));
            if(remote){
                socketChannel = SocketChannel.open();
                socketChannel.bind(new InetSocketAddress(config.read("zutil.log.ip"),
                        Integer.parseInt(config.read("zutil.log.port"))));
                socketChannel.connect(new InetSocketAddress(config.read("LogServer.ip"),
                        Integer.parseInt(config.read("LogServer.port"))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //connect();
    }

    private void connect() {
        try {
            if (socketChannel != null && socketChannel.isConnected()) {
                socketChannel.finishConnect();
                socketChannel.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getStackTrace的level取值，具有默认值
     */
    private int level = 3;

    @Override
    public void info(String msg, Object... args) {
        synchronized (NormalLog.class) {
            messageHandler(msg, args);
            logMsgConstructor("info");
            save();
        }
    }

    @Override
    public void remote(String msg, Object... args) {
        synchronized (NormalLog.class) {
            messageHandler(msg, args);
            logMsgConstructor("remote");
            save();
        }
    }

    @Override
    public void error(String msg, Object... args) {
        messageHandler(msg, args);
        logMsgConstructor("error");
        save();
    }

    @Override
    public void save() {
        checkLogFileExist();
        checkLogDate();
        saveToLog(preSave);
        if(remote){
            sendToRemoteServer();
        }
        logBuilder.delete(0, logBuilder.length());
    }

    private void sendToRemoteServer(){
        ReceiveLogReqVO receiveLogReqVO = new ReceiveLogReqVO();
        receiveLogReqVO.setLogData(new String(preSave.getBytes(StandardCharsets.UTF_8)));
        receiveLogReqVO.setRemoteIp(socketChannel.socket().getLocalAddress().toString());
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity();
        httpRequestEntity.setMethod("POST /log/receive");
        httpRequestEntity.setBody(new String(JSONTool.toJson(receiveLogReqVO)));
        String text = HttpParser.constructRequest(httpRequestEntity);
        byte[] respond = text.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(respond.length);
        buffer.put(respond);
        buffer.flip();
        try {
            //connect();
            socketChannel.write(buffer);
            Thread.sleep(10);
            //socketChannel.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logMsgConstructor(String type) {
        level = Integer.parseInt(config.read("system.normal.log.loglevel"));
        String className = Thread.currentThread().getStackTrace()[level].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[level].getMethodName();
        String fileName = Thread.currentThread().getStackTrace()[level].getFileName();
        int line = Thread.currentThread().getStackTrace()[level].getLineNumber();
        preSave = "[" + type.toUpperCase(Locale.ROOT)+" - "+ Thread.currentThread().getName() + "]"
                + fileName + "(" + className + "." + methodName + "." + line + ")----" + LocalDateTime.now() + "\n"
                + logBuilder.toString();
        System.out.println(preSave);
    }

    @SneakyThrows
    private void checkLogFileExist() {
        String logName = config.read("system.log.path") + "/" + config.read("system.name") + ".log";
        File file = new File(logName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private void checkLogDate() {
        LocalDate logDate = LocalDate.parse(config.read("system.normal.log.last"));
        if (logDate.until(LocalDate.now(), ChronoUnit.DAYS) >= 1) {
            renameLastLog();
            config.update("system.normal.log.last", LocalDate.now().toString(), config.read("sys.path"));
        }
    }

    @SneakyThrows
    private void renameLastLog() {
        String logName = config.read("system.log.path") + "/" + config.read("system.name") + ".log";
        String newName = config.read("system.log.path") + "/" + config.read("system.name") + "-" +
                config.read("system.normal.log.last") + ".log";
        File orin = new File(logName);
        orin.renameTo(new File(newName));
        orin = new File(logName);
        orin.createNewFile();
    }

    @SneakyThrows
    private void saveToLog(String log) {
        synchronized (NormalLog.class) {
            String logName = config.read("system.log.path") + "/" + config.read("system.name") + ".log";
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
                    (new FileOutputStream(logName,true),StandardCharsets.UTF_8));
            writer.write(log);
            writer.flush();
            writer.close();
        }
    }

    /**
     * 将格式化参数放入占位符中
     * ============================================================
     * *  算法：
     * * 每次循环脱离一对{}
     * * 若参数大于占位符数时保留{}
     * ============================================================
     *
     * @param msg  日志信息
     * @param args 格式化参数
     * @return @return {@link String }
     * @author zhl
     * @date 2021-09-15 20:24
     * @version V1.0
     */
    private void messageHandler(String msg, Object... args) {
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
        logBuilder.append(msg).append("\n");
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
