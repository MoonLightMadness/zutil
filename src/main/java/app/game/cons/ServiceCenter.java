package app.game.cons;

import app.game.BaseUserOnlineService;
import app.game.UserDataService;
import app.game.UserLogDataService;
import app.game.UserLogService;
import app.game.service.*;
import app.mapper.impl.Mapper;
import app.system.Core;

public class ServiceCenter {

    public static UserDataService userDataService = new UserDataServiceImpl();

    public static UserLogDataService userLogDataService = new UserLogDataServiceImpl();

    public static UserLogService userLogService = new UserLogServiceImpl();

    public static BaseUserDataServiceImpl baseUserDataService = new BaseUserDataServiceImpl();

    public static BaseUserOnlineServiceImpl baseUserOnlineService = new BaseUserOnlineServiceImpl();

    public static BaseUserLogDataServiceImpl baseUserLogDataService = new BaseUserLogDataServiceImpl();

    public static Mapper mapper = new Mapper(UserDataServiceImpl.class, Core.configer.read("mysql.path"));
}
