package com.example.wangruolan.randomlotteryanimation;

import android.app.Application;

/**
 * Created by wangruolan on 15-12-18.
 */
public class FrameApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseConfig.initDisplay(getApplicationContext());
    }
}
