package app.system;

import app.config.Config;
import app.config.impl.NormalConfig;
import app.log.Log;
import app.log.impl.NormalLog;

public class Core {

    public static Log log = new NormalLog();

    public static Config configer = new NormalConfig();

}
