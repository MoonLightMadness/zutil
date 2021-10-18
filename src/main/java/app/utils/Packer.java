package app.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : app.utils.Packer
 * @Description :
 * @Date 2021-09-27 08:44:31
 * @Author ZhangHL
 */
public class Packer<T> {


    public static<T> List<T> pack(Object[] objects) {
        List<T> res = new ArrayList<>();
        for (Object obj : objects) {
            res.add((T) obj);
        }
        return res;
    }

    public static <T> T pack(Object obj) {
        return (T) obj;
    }

}
