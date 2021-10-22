package app.db.impl;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.db.DataBase;
import app.log.Log;
import app.log.impl.NormalLog;
import app.system.Core;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MysqlImpl<T> implements DataBase<T> {
    private Connection connection;

    private Statement statement;

    private ResultSet resultSet;

    private Log log;

    private Config configer;

    private String dbName;

    @Override
    public void initialize(){
        log= Core.log;
        configer = new NormalConfig();
        //log.info(this.getClass().getName(),"初始化数据库连接");
        if(dbName == null){
            dbName=configer.read("database");
        }
        //log.info("连接到数据库:{}",dbName);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            log.info("数据库连接初始化完成");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error(this.getClass().getName(),e.getMessage());
        }
        firstContact();
    }

    /**
     * 初始化
     *
     * @param dbName 数据库的名字
     * @return
     * @author zhl
     * @date 2021-09-06 10:18
     * @version V1.0
     */
    @Override
    public void initialize(String dbName) {
        this.dbName = dbName;
        initialize();
    }

    private void open(){
        try {
            //jdbc:mysql://localhost:3306/test
            this.connection= DriverManager.getConnection("jdbc:mysql://"+dbName,
                    configer.read("mysql.user"),configer.read("mysql.password"));
            this.statement=connection.createStatement();
            //设置超时
            this.statement.setQueryTimeout(30);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void close(){
        try {
            connection.close();
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public T get(String command) {
        try {
            synchronized (this){
                open();
                resultSet= statement.executeQuery(command);
                if(resultSet.next()){
                    Object object =  resultSet.getObject(1);
                    close();
                    return (T) object;
                }
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
        close();
        return null;
    }

    @Override
    public Object[] getObjects(String command,String tableName,Class clazz){
        try {
            String[] names = getColumnNames(tableName);
            List<Object> list = new ArrayList<>();
            synchronized (this){
                open();
                resultSet= statement.executeQuery(command);
                while (resultSet.next()){
                    Object object = clazz.getDeclaredConstructor().newInstance();
                    Field[] fields = object.getClass().getDeclaredFields();
                    for (String name : names){
                        for (Field field : fields){
                            field.setAccessible(true);
                            if(name.replace("_","").equals(field.getName().toLowerCase(Locale.ROOT))){
                                String str = resultSet.getString(name);
                                field.set(object,str);
                            }
                        }
                    }
                    list.add(object);
                }
            }
            close();
            return list.toArray();
        } catch (SQLException | NoSuchMethodException throwables) {
            log.error("error:{}---sql:{}",throwables);
            throwables.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    @Override
    @SneakyThrows
    public Object getOneObject(String command,String tableName,Class clazz){
        try {
            Object object = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            String[] names = getColumnNames(tableName);
            synchronized (this){
                open();
                resultSet= statement.executeQuery(command);
                while (resultSet.next()){
                    for (String name : names){
                        for (Field field : fields){
                            field.setAccessible(true);
                            if(name.replace("_","").equals(field.getName().toLowerCase(Locale.ROOT))){
                                String str = resultSet.getString(name);
                                field.set(object,str);
                            }
                        }
                    }
                    close();
                    return object;
                }
            }
        } catch (SQLException | NoSuchMethodException throwables) {
            log.error("error:{}---sql:{}",throwables);
            throwables.printStackTrace();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        close();
        return null;
    }

    private String[] getColumnNames(String tableName){
        String comm = "select column_name from information_schema.COLUMNS where table_name=\'"+tableName+"\'";
        StringBuilder sb = new StringBuilder();
        try {
            open();
            resultSet = statement.executeQuery(comm);
            while (resultSet.next()){
                sb.append(resultSet.getString("column_name")).append("\n");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        close();
        return sb.toString().split("\n");
    }

    @Override
    public T[] gets(String command) {
        List<T> list=new ArrayList<>();
        int count=1;
        try {
            synchronized (this){
                open();
                resultSet= statement.executeQuery(command);
                while (resultSet.next()){
                    list.add((T) resultSet.getObject(count));
                    count++;
                }
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
        }
        close();
        return (T[]) list.toArray();
    }

    @Override
    @SneakyThrows
    public void insert(String command) {
        try {
            synchronized (this){
                open();
                statement.execute(command);
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),throwables.getSQLState());
            throwables.printStackTrace();
            throw throwables;
        }
        close();
    }

    @Override
    public void delete(String command) {
        try {
            synchronized (this){
                open();
                statement.execute(command);
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),command);
            throwables.printStackTrace();
        }
        close();
    }

    @Override
    public void update(String command) {
        try {
            synchronized (this){
                open();
                statement.execute(command);
            }
        } catch (SQLException throwables) {
            log.error(this.getClass().getName(),"error:{}---sql:{}",throwables.getMessage(),command);
            throwables.printStackTrace();
        }
        close();
    }

    @Override
    public Object firstContact() {
        long start = System.currentTimeMillis();
        open();
        try {
            resultSet = statement.executeQuery("SELECT VERSION()");
            if (resultSet.next()){
                String version = resultSet.getString(1);
                log.info("First Contact:~Version:{} Cost:{}ms",version,System.currentTimeMillis() - start);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
}
