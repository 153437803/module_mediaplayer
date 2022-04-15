package lib.kalu.mediaplayer.core.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import lib.kalu.mediaplayer.core.controller.base.ControllerLayout;
import lib.kalu.mediaplayer.util.PlayerUtils;

/**
 * @description: 播放器帮助类
 */
public class VideoHelper {

    private static VideoHelper sInstance;

    /**
     * 构造方法，避免直接new
     */
    private VideoHelper() {
        //避免初始化
    }


    /**
     * 一定要使用单例模式，保证同一时刻只有一个视频在播放，其他的都是初始状态
     * 单例模式
     *
     * @return VideoPlayerManager对象
     */
    public static VideoHelper instance() {
        if (sInstance == null) {
            synchronized (VideoHelper.class) {
                if (sInstance == null) {
                    sInstance = new VideoHelper();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取DecorView
     *
     * @param context         上下文
     * @param videoController controller
     */
    protected ViewGroup getDecorView(Context context, ControllerLayout videoController) {
        Activity activity = VideoHelper.instance().getActivity(context, videoController);
        if (activity == null) {
            return null;
        }
        return (ViewGroup) activity.getWindow().getDecorView();
    }

    /**
     * 获取activity中的content view,其id为android.R.id.content
     *
     * @param context         上下文
     * @param videoController controller
     */
    protected ViewGroup getContentView(Context context, ControllerLayout videoController) {
        Activity activity = VideoHelper.instance().getActivity(context, videoController);
        if (activity == null) {
            return null;
        }
        return activity.findViewById(android.R.id.content);
    }


    /**
     * 获取Activity，优先通过Controller去获取Activity
     *
     * @param context         上下文
     * @param videoController controller
     * @return
     */
    protected Activity getActivity(Context context, ControllerLayout videoController) {
        Activity activity;
        if (videoController != null) {
            activity = PlayerUtils.scanForActivity(videoController.getContext());
            if (activity == null) {
                activity = PlayerUtils.scanForActivity(context);
            }
        } else {
            activity = PlayerUtils.scanForActivity(context);
        }
        return activity;
    }

    /**
     * 显示NavigationBar和StatusBar
     *
     * @param decorView       decorView
     * @param context         上下文
     * @param videoController controller
     */
    protected void showSysBar(ViewGroup decorView, Context context, ControllerLayout videoController) {
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
        VideoHelper.instance().getActivity(context, videoController)
                .getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 隐藏NavigationBar和StatusBar
     *
     * @param decorView       decorView
     * @param context         上下文
     * @param videoController controller
     */
    protected void hideSysBar(ViewGroup decorView, Context context, ControllerLayout videoController) {
        int uiOptions = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decorView.setSystemUiVisibility(uiOptions);
        VideoHelper.instance().getActivity(context, videoController).getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /**
     * 判断是否为本地数据源，包括 本地文件、Asset、raw
     *
     * @param url                 url地址
     * @param assetFileDescriptor assets文件
     * @return 是否为本地数据源
     */
    protected boolean isLocalDataSource(CharSequence url, AssetFileDescriptor assetFileDescriptor) {
        if (assetFileDescriptor != null) {
            return true;
        } else if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url.toString());
            return ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())
                    || ContentResolver.SCHEME_FILE.equals(uri.getScheme())
                    || "rawresource".equals(uri.getScheme());
        }
        return false;
    }


}
