package com.jotangi.greentravel;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class Global {

    public static SimpleDateFormat DateFormatAmPm   = new SimpleDateFormat("yyyy年M月d日 aa H時m分", Locale.TAIWAN);
    public static SimpleDateFormat DateFormatFrom   = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
    public static SimpleDateFormat DateFormatToDate = new SimpleDateFormat("yyyy年M月d日", Locale.TAIWAN);


    public static String ACCOUNT           = "";

    public static String TEMP_ACCOUNT      = "";

    public static String ACCESS_TOKEN = "cLEzfgz5c5hxQwLWauCOdAilwgfn97yj";

    public static String ACCOUNT_TYPE = "0";

    public static String NOTIFICATION_TOKEN = "";

    public static PersonalData personalData;

    public static String BOSS_OWNERSTOREID;

}
