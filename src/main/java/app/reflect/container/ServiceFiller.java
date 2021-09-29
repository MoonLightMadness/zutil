package app.reflect.container;

import app.reflect.domain.ReflectIndicator;
import app.reflect.domain.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @ClassName : app.reflect.container.ServiceFiller
 * @Description :
 * @Date 2021-09-29 08:10:51
 * @Author ZhangHL
 */
public class ServiceFiller {

    private volatile List<ServiceInfo> infos;

    public ServiceFiller(){
        infos = new ArrayList<>();
    }

    /**
     * 添加反射指示器到集合中
     *
     * @param
     * @return
     * @author zhl
     * @date 2021-08-13 21:17
     * @version V1.0
     */
    public void add(ServiceInfo info) {
        synchronized (ServiceFiller.class){
            ListIterator<ServiceInfo> iterator = infos.listIterator();
            iterator.add(info);
        }
    }

    /**
     * 添加一个反射指示器集合到原集合中
     *
     * @return
     * @author zhl
     * @date 2021-08-13 21:17
     * @version V1.0
     */
    public void add(List<ServiceInfo> info) {
        synchronized (ServiceFiller.class){
            ListIterator<ServiceInfo> iterator = info.listIterator();
            while (iterator.hasNext()) {
                ServiceInfo temp = iterator.next();
                add(temp);
            }
        }
    }

    /**
     * 在集合中删除反射指示器
     *
     * @return
     * @author zhl
     * @date 2021-08-13 21:18
     * @version V1.0
     */
    public void delete(ServiceInfo info) {
        ListIterator<ServiceInfo> iterator = infos.listIterator();
        while (iterator.hasNext()) {
            ServiceInfo temp = iterator.next();
            if (temp == info) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * 根据抽象路径删除反射指示器
     *
     * @return
     * @author zhl
     * @date 2021-08-13 21:20
     * @version V1.0
     */
    public void delete(String className) {
        ListIterator<ServiceInfo> iterator = infos.listIterator();
        while (iterator.hasNext()) {
            ServiceInfo temp = iterator.next();
            if (temp.getClassName().equals(className)) {
                iterator.remove();
                break;
            }
        }
    }



    /**
     * 返回一个集合的迭代器
     *
     * @return @return {@link ListIterator<ReflectIndicator> }
     * @author zhl
     * @date 2021-08-13 21:37
     * @version V1.0
     */
    public ListIterator<ServiceInfo> getIterator() {
        return infos.listIterator();
    }

}
