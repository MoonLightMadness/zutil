package app.game.cons;

import app.game.UserDataService;
import app.game.UserLogDataService;
import app.game.UserLogService;
import app.game.service.UserDataServiceImpl;
import app.game.service.UserLogDataServiceImpl;
import app.game.service.UserLogServiceImpl;

public class ServiceCenter {

    public static UserDataService userDataService = new UserDataServiceImpl();

    public static UserLogDataService userLogDataService = new UserLogDataServiceImpl();

    public static UserLogService userLogService = new UserLogServiceImpl();
}
