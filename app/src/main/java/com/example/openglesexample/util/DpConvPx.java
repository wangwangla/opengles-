package com.example.openglesexample.util;

import android.content.Context;

public class DpConvPx {
    public static float dp2px(Context context,int dp){
        return context.getResources().getDisplayMetrics().density * dp;
    }
}
