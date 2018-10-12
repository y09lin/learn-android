package com.huim_lin.learn.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Context ctx;

    public static void init(Context context){
        ctx = context;
    }

    public static void showText(String text){
        if (ctx == null){
            return;
        }
        Toast.makeText(ctx,text,Toast.LENGTH_SHORT).show();
    }
}
