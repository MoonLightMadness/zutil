package app.log;

import app.log.impl.NormalLog;

public class LogFactory {

    public static NormalLog getNormalLog(){
        return new NormalLog();
    }


}
