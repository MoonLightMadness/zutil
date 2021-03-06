package app.reflect.domain;

import lombok.Data;

/**
 * @ClassName : app.utils.datastructure.ReflecIndicator
 * @Description :
 * @Date 2021-08-13 08:21:44
 * @Author ZhangHL
 */
@Data
public class ReflectIndicator {

    /**
     * 类全路径
     */
    private String classPath;


    /**
     * 相对路径(抽象路径)
     */
    private String relativePath;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 权限
     */
    private String authority;

    /**
     * 触发方式
     * 0-本地 1-远程
     */
    private String approachWay;

    /**
     * 远程触发方法的ip
     */
    private String approachIP;

    /**
     * 远程触发方法的端口
     */
    private String approachPort;

    /**
     * 参数类型
     */
    private String[] parameterTypes;

}
