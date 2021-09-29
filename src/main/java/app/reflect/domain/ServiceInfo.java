package app.reflect.domain;

import lombok.Data;

/**
 * @ClassName : app.reflect.domain.ServiceInfo
 * @Description :
 * @Date 2021-09-29 08:11:27
 * @Author ZhangHL
 */
@Data
public class ServiceInfo {

    private String className;

    private String classPath;

    private Object obj;


}
