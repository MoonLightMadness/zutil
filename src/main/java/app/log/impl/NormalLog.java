package app.log.impl;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.config.impl.SystemConfig;
import app.log.Log;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class NormalLog implements Log {

    private Config config = new SystemConfig();

    private StringBuilder logBuilder = new StringBuilder();

    private String preSave;

    /**
     * getStackTrace的level取值，具有默认值
     */
    private int level = 3;

    @Override
    public void info(String msg, Object... args) {
        messageHandler(msg, args);
        logMsgConstructor("info");
        save();
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
        preSave = null;
    }

    private void logMsgConstructor(String type) {
        level = Integer.parseInt(config.read("system.normal.log.loglevel"));
        String className = Thread.currentThread().getStackTrace()[level].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[level].getMethodName();
        String fileName = Thread.currentThread().getStackTrace()[level].getFileName();
        int line = Thread.currentThread().getStackTrace()[level].getLineNumber();
        preSave = "[" + type.toUpperCase(Locale.ROOT) + "]"
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(logName, true));
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
