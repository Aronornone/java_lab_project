package utils;

import org.apache.log4j.Logger;

public class ServletLog {
    private static final Logger lgServ=Logger.getLogger("Serv");
    private static final Logger lgReg=Logger.getLogger("Reg");
    private static final Logger lgDB=Logger.getLogger("DB");
    private ServletLog(){}

    public static Logger getLgServ() {
        return lgServ;
    }

    public static Logger getLgReg() {
        return lgReg;
    }

    public static Logger getLgDB() {
        return lgDB;
    }
}
