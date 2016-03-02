package ca.vire.otp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date TimeStamp = new Date(); 
        return sdfDate.format(TimeStamp);        
    }

}
