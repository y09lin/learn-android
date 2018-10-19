package com.huim_lin.learn.util;

public class TimeUtil {

    public static String getPlayTime(long millSecond){
        long m = millSecond / 1000 / 60;
        long s = millSecond / 1000 - m * 60;
        String ms = m<10? "0"+m : m+"";
        String ss = s<10? "0"+s : s+"";
        return ms+":"+ss;
    }
}
