package app.db;

import app.db.impl.MysqlImpl;
import app.db.impl.SqliteImpl;
import app.system.Core;

public class DataBaseFactory {

    public static DataBase getInstance(){
        String type = Core.configer.read("db.type");
        if("sqlite".equals(type)){
            return new SqliteImpl();
        }
        if("mysql".equals(type)){
            return new MysqlImpl();
        }
        return null;
    }

}
