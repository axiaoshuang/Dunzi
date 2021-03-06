package com.win.dunzi;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;


/**
 * author：WangShuang
 * Date: 2016/1/25 12:54
 * email：m15046658245_1@163.com
 */
public class BaseApplication  extends Application{
    //做初始化工作
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        ActiveAndroid.initialize(this);
        ShareSDK.initSDK(this);


        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
        //清理工作
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
