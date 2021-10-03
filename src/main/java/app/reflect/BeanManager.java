package app.reflect;

public class BeanManager {

    private static BeanCenter beanCenter;


    private static void load(){
        if(beanCenter == null){
            beanCenter = new BeanCenter();
            beanCenter.load();
        }
    }

    public static Object get(String name){
        load();
        return beanCenter.get(name);
    }


}
