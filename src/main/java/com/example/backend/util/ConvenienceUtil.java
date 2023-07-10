package com.example.backend.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ConvenienceUtil {

    public static String currentTimestamp(){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(currentTimestamp);
    }
}
