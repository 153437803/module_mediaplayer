package lib.kalu.mediaplayer.config;

import androidx.annotation.IntDef;
import androidx.annotation.Keep;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Keep
public class PlayerType {

    /**
     * 播放模式
     * 普通模式，小窗口模式，正常模式三种其中一种
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface WindowType {
        //普通模式
        int NORMAL = 1001;
        //全屏模式
        int FULL = 1002;
        //小屏模式
        int TINY = 1003;

        @IntDef({NORMAL, FULL, TINY})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /***********************/

    /**
     * 播放状态，主要是指播放器的各种状态
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateType {
        int STATE_RELEASE = 2000;
        int STATE_IDLE = 2001; // 播放未开始，即将进行
        int STATE_PREPARING = 2002; // 播放准备中
        int STATE_PREPARED = 2003; // 播放准备就绪
        int STATE_PLAYING = 2004; // 正在播放
        int STATE_PAUSED = 2005; // 暂停播放
        int STATE_BUFFERING_PLAYING = 2006; // 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
        int STATE_BUFFERING_PAUSED = 2007; // 暂停缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
        int STATE_COMPLETED = 2008; // 播放完成
        int STATE_START_ABORT = 2009; // 开始播放中止
        int STATE_ONCE_LIVE = 2010; // 即将开播

        int STATE_URL_NULL = 2011; // 链接为空
        int STATE_PARSE_ERROR = 2012; // 解析异常
        int STATE_NETWORK_ERROR = 2013; // 播放错误，网络异常
        int STATE_ERROR = 2014; // 播放错误

        @IntDef({STATE_RELEASE, STATE_ERROR, STATE_IDLE, STATE_PREPARING,
                STATE_PREPARED, STATE_PLAYING, STATE_PAUSED,
                STATE_BUFFERING_PLAYING, STATE_BUFFERING_PAUSED,
                STATE_COMPLETED, STATE_START_ABORT, STATE_NETWORK_ERROR,
                STATE_PARSE_ERROR, STATE_URL_NULL, STATE_ONCE_LIVE})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /**************/

    /**
     * 播放视频缩放类型
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleType {
        int SCREEN_SCALE_DEFAULT = 3001; //默认类型
        int SCREEN_SCALE_16_9 = 3002; //16：9比例类型，最为常见
        int SCREEN_SCALE_4_3 = 3003;  //4：3比例类型，也比较常见
        int SCREEN_SCALE_MATCH_PARENT = 3004; //充满整个控件视图
        int SCREEN_SCALE_ORIGINAL = 3005; //原始类型，指视频的原始类型
        int SCREEN_SCALE_CENTER_CROP = 3006; //剧中裁剪类型

        @IntDef({SCREEN_SCALE_DEFAULT, SCREEN_SCALE_16_9,
                SCREEN_SCALE_4_3, SCREEN_SCALE_MATCH_PARENT,
                SCREEN_SCALE_ORIGINAL, SCREEN_SCALE_CENTER_CROP})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /********************/

    /**
     * 播放状态
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusType {
        //播放完成
        int COMPLETED = 4001;
        //正在播放
        int PLAYING = 4002;
        //暂停状态
        int PAUSE = 4003;
        //用户点击back。当视频退出全屏或者退出小窗口后，再次点击返回键，让用户自己处理返回键事件的逻辑
        int BACK_CLICK = 4004;

        @IntDef({COMPLETED, PLAYING, PAUSE, BACK_CLICK})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /*****************/

    /**
     * 通过注解限定类型
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlatformType {
        int NATIVE = 5001; // MediaPlayer，基于原生自带的播放器控件
        int EXO = 5002; // 基于谷歌视频播放器
        int IJK = 5003; // IjkPlayer，基于IjkPlayer封装播放器

        @IntDef({IJK, NATIVE, EXO})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /***********************/

    /**
     * 通过注解限定类型
     * 加载loading的类型
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadingType {
        int LOADING_RING = 6001; // 是仿腾讯加载loading，其实是帧动画
        int LOADING_QQ = 6002; // 是转圈加载loading，是补间动画

        @IntDef({LOADING_RING, LOADING_QQ})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /*********************/

    /**
     * 控制器上的视频顶部View点击事件
     * 在竖屏模式下，默认是不显示，需要显示设置controller.setTopVisibility(true);
     * 在横屏模式下，默认是不显示，需要显示设置controller.setTvAndAudioVisibility(true,true);
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface ControllerType {
        int DOWNLOAD = 7001; // 下载
        int AUDIO = 7002; // 切换音频
        int SHARE = 7003; // 分享
        int MENU = 7004; // 菜单
        int TV = 7005; // TV，点击投影到电视上
        int HOR_AUDIO = 7006; // 音频

        @IntDef({DOWNLOAD, AUDIO,
                SHARE, MENU, TV,
                HOR_AUDIO})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /******************/

    /**
     * 电量状态
     */
    @Keep
    @Retention(RetentionPolicy.SOURCE)
    public @interface BatterType {
        int BATTERY_CHARGING = 8001;
        int BATTERY_FULL = 8002;
        int BATTERY_10 = 8003;
        int BATTERY_20 = 8004;
        int BATTERY_50 = 8005;
        int BATTERY_80 = 8006;
        int BATTERY_100 = 8007;

        @IntDef({BATTERY_CHARGING, BATTERY_FULL, BATTERY_10, BATTERY_20, BATTERY_50, BATTERY_80, BATTERY_100})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /********************/

    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface ErrorType {
        //错误的链接
        int ERROR_RETRY = 9001;
        //错误的链接
        int ERROR_SOURCE = 9002;
        //解析异常
        int ERROR_PARSE = 9003;
        //其他异常
        int ERROR_UNEXPECTED = 9004;

        @IntDef({ERROR_RETRY, ERROR_SOURCE, ERROR_PARSE, ERROR_UNEXPECTED})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    /*****************/


    @Retention(RetentionPolicy.SOURCE)
    @Keep
    public @interface MediaType {
        int MEDIA_INFO_URL_NULL = -1; // 视频传入url为空
        int MEDIA_INFO_VIDEO_RENDERING_START = 3; // 开始渲染视频画面
        int MEDIA_INFO_BUFFERING_START = 701; // 缓冲开始
        int MEDIA_INFO_BUFFERING_END = 702; // 缓冲结束
        int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001; // 视频旋转信息

        @IntDef({MEDIA_INFO_URL_NULL, MEDIA_INFO_VIDEO_RENDERING_START, MEDIA_INFO_BUFFERING_START, MEDIA_INFO_BUFFERING_END, MEDIA_INFO_VIDEO_ROTATION_CHANGED})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }
}