package app.config;

/**
 * 配置初始化
 *
 * @author zhl
 * @date 2021-09-18 09:58
 */
public interface IConfigInitializer {

    /**
     * 扫描程序以加载配置路径
     * @return
     * @author zhl
     * @date 2021-09-18 09:59
     * @version V1.0
     */
    void loadConfigPath(String[] packages);

    /**
     * 从给的路径中加载配置
     * @return
     * @author zhl
     * @date 2021-09-18 10:01
     * @version V1.0
     */
    void loadConfigFromPath();

}
