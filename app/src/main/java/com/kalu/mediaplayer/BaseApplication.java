package com.kalu.mediaplayer;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import lib.kalu.mediaplayer.cache.config.CacheConfig;
import lib.kalu.mediaplayer.cache.config.CacheConfigManager;
import lib.kalu.mediaplayer.cache.config.CacheType;
import lib.kalu.mediaplayer.kernel.video.utils.PlayerConstant;
import lib.kalu.mediaplayer.kernel.video.utils.PlayerFactoryUtils;
import lib.kalu.mediaplayer.keycode.KeycodeImplSimulator;
import lib.kalu.mediaplayer.ui.config.PlayerConfig;
import lib.kalu.mediaplayer.ui.config.PlayerConfigManager;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;

    public static synchronized BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    public BaseApplication() {
    }

    /**
     * 这个最先执行
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 程序启动的时候执行
     */
    @Override
    public void onCreate() {
        Log.d("Application", "onCreate");
        super.onCreate();
        instance = this;
        ScreenDensityUtils.setup(this);
        ScreenDensityUtils.register(this, 375.0f, ScreenDensityUtils.MATCH_BASE_WIDTH, ScreenDensityUtils.MATCH_UNIT_DP);

        initVideoUI();
        initVideoCache();
    }

    private void initVideoUI() {
        //播放器配置，注意：此为全局配置，按需开启
        PlayerConfig build = PlayerConfig.newBuilder()
                //设置视频全局埋点事件
                .setBuriedPointEvent(new BuriedPointEventImpl())
                //调试的时候请打开日志，方便排错
                .setLogEnabled(true)
                //设置ijk
                .setPlayerFactory(PlayerFactoryUtils.getPlayer(PlayerConstant.PlayerType.TYPE_EXO))
                //创建SurfaceView
                //.setRenderViewFactory(SurfaceViewFactory.create())
                .setKeycodeImpl(new KeycodeImplSimulator())
                .build();
        PlayerConfigManager.getInstance().setConfig(build);
    }

    private void initVideoCache() {
        CacheConfig build = new CacheConfig.Build()
                .setIsEffective(true)
                .setType(CacheType.ROM)
                .setCacheMax(1000)
                .setLog(false)
                .build();
        CacheConfigManager.getInstance().setConfig(build);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        Log.d("Application", "onTerminate");
        super.onTerminate();
    }

    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        Log.d("Application", "onLowMemory");
        super.onLowMemory();
    }

    /**
     * HOME键退出应用程序
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        Log.d("Application", "onTrimMemory");
        super.onTrimMemory(level);
    }

    /**
     * onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Application", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }
}