package com.jotangi.jotangi2022;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 😀😀😀😀😀😀😀😀😀😀😀😀Kelly Note!!!!😀😀😀😀😀😀😀😀😀😀😀😀
 * =============================================================
 * all feeback true || false
 *
 * */
public class RegularExceptionUtils {

    /*
     *check mail
     * 檢查 郵箱
     *
     */
    public static boolean mailCheck(String Mail) {
        String mailString = Mail;
        boolean result;
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(Mail);
        if (matcher.matches() != true || mailString.equals("")) { result = false;
        } else { result = true; }
        return result;
    }

    /*
     *check ID
     * 檢查身分證
     *
     * */
    public static boolean idCheck(String Id) {
        String IdString = Id;
        boolean result;
        Pattern pattern;
        Matcher matcher;
        final String Id_PATTERN = "^([A-Z])([1-2]\\d{8})$";
        pattern = Pattern.compile(Id_PATTERN);
        matcher = pattern.matcher(Id);
        if (matcher.matches() != true || IdString.equals("") || IdString.length() != 10) {
            result=false;
        } else {
            result=true;
        }
        return  result;
    }
    /*
     * check telephone
     *檢查電話號碼
     * */
    public static boolean phoneCheck(String phone) {
        String phoneString = phone;
        boolean result;
        Pattern pattern;
        Matcher matcher;
        final String Id_PATTERN = "09\\d{2}(\\d{6}|-\\d{3}-\\d{3})";
        pattern = Pattern.compile(Id_PATTERN);
        matcher = pattern.matcher(phone);
        if (matcher.matches() != true || phoneString.equals("")) {
            result=false;
        } else { result=true; }
        return  result;
    }

    /*
     * check !? chinese
     *檢查中文
     * */
    public static boolean chineseCheck(String chinese) {
        String chineseString = chinese;
        Pattern pattern;
        Matcher matcher;
        boolean result;
        final String Id_PATTERN = "^[\\u4e00-\\u9fa5]{0,}$";
        pattern = Pattern.compile(Id_PATTERN);
        matcher = pattern.matcher(chinese);
        if (matcher.matches() != true || chineseString.equals("")) {
            result=false;
        } else { result=true; }
        return result;
    }

    /*
     * check !? english
     *檢查英文
     * */
    public static boolean englishCheck(String english) {
        String englishString = english;
        boolean result;
        Pattern pattern;
        Matcher matcher;
        final String Id_PATTERN = "^[A-Za-z]+$";
        pattern = Pattern.compile(Id_PATTERN);
        matcher = pattern.matcher(english);
        if (matcher.matches() != true || englishString.equals("")) {
            result=false;
        } else {
            result=true;
        }
        return  result;
    }

    /*
     * check !? english or number
     *檢查 英文或數字
     * */
    public static boolean EnNuCheck(String EnNustring) {
        String EnNustringString = EnNustring;
        Pattern pattern;
        Matcher matcher;
        boolean result;
        final String Id_PATTERN = "^[A-Za-z0-9]+$";
        pattern = Pattern.compile(Id_PATTERN);
        matcher = pattern.matcher(EnNustring);
        if (matcher.matches() != true || EnNustringString.equals("")) {
            result=false;
        } else {
            result=true;
        }
        return  result;
    }

    /*
     * check !?  number
     *檢查數字
     * */
    public static boolean numberCheck(String number) {
        String numberString = number;
        boolean result;
        Pattern pattern;
        Matcher matcher;
        final String Id_PATTERN = "^[0-9]+$";
        pattern = Pattern.compile(Id_PATTERN);
        matcher = pattern.matcher(numberString);
        if (matcher.matches() != true || numberString.equals("")) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }
}
