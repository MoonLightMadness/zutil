package app.log;

public interface Log {

    /**
     * 记录日志信息
     * @param msg  日志信息
     * @param args 格式化参数
     * @return
     * @author zhl
     * @date 2021-09-15 20:20
     * @version V1.0
     */
    void info(String msg,Object... args);


    /**
     * 记录错误日志信息
     * @param msg  日志信息
     * @param args 格式化参数
     * @return
     * @author zhl
     * @date 2021-09-15 20:20
     * @version V1.0
     */
    void error(String msg,Object... args);

    /**
     * 记录远程日志信息
     * @param msg
     * @param args
     * @return
     * @author zhl
     * @date 2021-10-02 12:57
     * @version V1.0
     */
    void remote(String msg,Object... args);

    /**
     * 持久化保存日志
     * @return
     * @author zhl
     * @date 2021-09-15 20:22
     * @version V1.0
     */
    void save();

}
