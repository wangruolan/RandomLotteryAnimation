package com.example.wangruolan.randomlotteryanimation;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by wangruolan on 15-12-4.
 */
public class BaseConfig {
    public static int width;
    public static int height;
    public static float density;
    public static int densityDpi;

    public static void initDisplay(Context context) {
        if (context.getResources() != null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            density = metrics.density;
            densityDpi = metrics.densityDpi;
        }

    }
}
