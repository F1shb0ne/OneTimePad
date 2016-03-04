package ca.vire.otp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

    public static String getTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date TimeStamp = new Date(); 
        return sdfDate.format(TimeStamp);        
    }

}
