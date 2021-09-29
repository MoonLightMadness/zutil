package app.game.cons;

import app.game.*;
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

    public static BaseCharacterConfigService baseCharacterConfigService = new BaseCharacterConfigServiceImpl();

    public static UserBagService userBagService = new UserBagServiceImpl();

    public static BaseItemConfigService baseItemConfigService = new BaseItemConfigServiceImpl();

    public static Mapper mapper = new Mapper(UserDataServiceImpl.class, Core.configer.read("mysql.path"));
}
