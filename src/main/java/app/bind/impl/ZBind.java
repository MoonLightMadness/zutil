package app.bind.impl;

import app.bind.Bind;
import app.bind.BindUnit;
import app.bind.enums.BindEnums;
import app.bind.exception.BindException;
import app.log.Log;
import app.system.Core;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : app.bind.impl.ZBind
 * @Description :
 * @Date 2021-09-16 14:14:25
 * @Author ZhangHL
 */
public class ZBind implements Bind {

    private Map<String, List<BindUnit>> binds = new HashMap<>();

    private Log log = Core.log;

    @Override
    public void createNewBind(String key) {
        log.info("创建新绑定,入参{}",key);
        binds.put(key, new ArrayList<>());
        log.info("新绑定创建成功");
    }

    @Override
    public void register(String key, BindUnit unit) {
        checkKeyExist(key);
        List<BindUnit> value = binds.get(key);
        value.add(unit);
    }

    @Override
    public void notifyAllUnit(String key, Object obj, String... args) {
        log.info("触发订阅方法，开始,key:{}",key);
        checkKeyExist(key);
        List<BindUnit> value = binds.get(key);
        for (BindUnit bu : value) {
            bu.invoke(obj, args);
        }
        log.info("订阅方法触发完成");
    }

    @SneakyThrows
    private void checkKeyExist(String key) {
        if (!binds.containsKey(key)) {
            throw new BindException(BindEnums.BE_001.getCode(), BindEnums.BE_001.getMsg());
        }
    }
}
