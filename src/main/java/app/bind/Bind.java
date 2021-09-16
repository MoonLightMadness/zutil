package app.bind;

public interface Bind {

    void createNewBind(String key);

    void register(String key, BindUnit unit);

    void notifyAllUnit(String key, Object obj, String... args);

}
