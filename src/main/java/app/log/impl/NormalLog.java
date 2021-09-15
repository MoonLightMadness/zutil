package app.log.impl;

import app.log.Log;

public class NormalLog implements Log {

    private StringBuilder logBuilder = new StringBuilder();

    @Override
    public void info(String msg, Object... args) {

    }

    @Override
    public void error(String msg, Object... args) {

    }

    @Override
    public void save() {

    }

    /**
     * 将格式化参数放入占位符中
     * ============================================================
     *              *  算法：
     *              * 每次循环脱离一对{}
     *              * 若参数大于占位符数时保留{}
     * ============================================================
     * @param msg  日志信息
     * @param args 格式化参数
     * @return @return {@link String }
     * @author zhl
     * @date 2021-09-15 20:24
     * @version V1.0
     */
    private void messageHandler(String msg,Object... args){
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
