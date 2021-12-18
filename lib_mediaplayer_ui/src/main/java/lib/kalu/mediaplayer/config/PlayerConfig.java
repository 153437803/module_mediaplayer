package lib.kalu.mediaplayer.config;


import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.buried.BuriedPointEvent;
import lib.kalu.mediaplayer.core.kernel.video.factory.PlayerFactory;
import lib.kalu.mediaplayer.core.kernel.video.platfrom.media.MediaPlayerFactory;
import lib.kalu.mediaplayer.keycode.KeycodeImpl;
import lib.kalu.mediaplayer.keycode.KeycodeImplTV;
import lib.kalu.mediaplayer.core.player.ProgressManager;
import lib.kalu.mediaplayer.widget.surface.SurfaceFactory;
import lib.kalu.mediaplayer.widget.surface.TextureViewFactory;


/**
 * @description: 播放器全局配置
 * @date: 2021-05-12 14:43
 */
@Keep
public class PlayerConfig {

    public final boolean mPlayOnMobileNetwork;
    public final boolean mEnableOrientation;
    public final boolean mIsEnableLog;
    public final ProgressManager mProgressManager;
    public final PlayerFactory mPlayerFactory;
    public final BuriedPointEvent mBuriedPointEvent;
    public final int mScreenScaleType;
    public final SurfaceFactory mRenderViewFactory;
    public final boolean mAdaptCutout;
    public final boolean mIsShowToast;
    public final long mShowToastTime;
    public final KeycodeImpl mKeycodeImpl;

    private PlayerConfig(Builder builder) {
        mIsEnableLog = builder.mIsEnableLog;
        mEnableOrientation = builder.mEnableOrientation;
        mPlayOnMobileNetwork = builder.mPlayOnMobileNetwork;
        mProgressManager = builder.mProgressManager;
        mScreenScaleType = builder.mScreenScaleType;
        if (null == builder.mKeycodeImpl) {
            mKeycodeImpl = new KeycodeImplTV();
        } else {
            mKeycodeImpl = builder.mKeycodeImpl;
        }
        if (builder.mPlayerFactory == null) {
            //默认为AndroidMediaPlayer
            mPlayerFactory = MediaPlayerFactory.create();
        } else {
            mPlayerFactory = builder.mPlayerFactory;
        }
        mBuriedPointEvent = builder.mBuriedPointEvent;
        if (builder.mRenderViewFactory == null) {
            //默认使用TextureView渲染视频
            mRenderViewFactory = TextureViewFactory.create();
        } else {
            mRenderViewFactory = builder.mRenderViewFactory;
        }
        mAdaptCutout = builder.mAdaptCutout;
        mIsShowToast = builder.mIsShowToast;
        mShowToastTime = builder.mShowToastTime;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Keep
    public final static class Builder {

        /**
         * 默认是关闭日志的
         */
        private boolean mIsEnableLog = false;
        /**
         * 在移动环境下调用start()后是否继续播放，默认不继续播放
         */
        private boolean mPlayOnMobileNetwork;
        /**
         * 是否监听设备方向来切换全屏/半屏， 默认不开启
         */
        private boolean mEnableOrientation;
        /**
         * 设置进度管理器，用于保存播放进度
         */
        private ProgressManager mProgressManager;
        /**
         * 自定义播放核心
         */
        private PlayerFactory mPlayerFactory;
        /**
         * 自定义视频全局埋点事件
         */
        private BuriedPointEvent mBuriedPointEvent;
        /**
         * 设置视频比例
         */
        private int mScreenScaleType;
        /**
         * 自定义RenderView
         */
        private SurfaceFactory mRenderViewFactory;
        /**
         * 是否适配刘海屏，默认适配
         */
        private boolean mAdaptCutout = true;
        /**
         * 是否设置倒计时n秒吐司
         */
        private boolean mIsShowToast = false;
        /**
         * 倒计时n秒时间
         */
        private long mShowToastTime = 5;

        private KeycodeImpl mKeycodeImpl;

        public KeycodeImpl getKeycodeImpl() {
            return mKeycodeImpl;
        }

        public Builder setKeycodeImpl(KeycodeImpl mKeycodeImpl) {
            this.mKeycodeImpl = mKeycodeImpl;
            return this;
        }

        /**
         * 是否监听设备方向来切换全屏/半屏， 默认不开启
         */
        public Builder setEnableOrientation(boolean enableOrientation) {
            mEnableOrientation = enableOrientation;
            return this;
        }

        /**
         * 在移动环境下调用start()后是否继续播放，默认不继续播放
         */
        public Builder setPlayOnMobileNetwork(boolean playOnMobileNetwork) {
            mPlayOnMobileNetwork = playOnMobileNetwork;
            return this;
        }

        /**
         * 设置进度管理器，用于保存播放进度
         */
        public Builder setProgressManager(@Nullable ProgressManager progressManager) {
            mProgressManager = progressManager;
            return this;
        }

        /**
         * 是否打印日志
         */
        public Builder setLogEnabled(boolean enableLog) {
            mIsEnableLog = enableLog;
            return this;
        }

        /**
         * 自定义播放核心
         */
        public Builder setPlayerFactory(PlayerFactory playerFactory) {
            mPlayerFactory = playerFactory;
            return this;
        }

        /**
         * 自定义视频全局埋点事件
         */
        public Builder setBuriedPointEvent(BuriedPointEvent buriedPointEvent) {
            mBuriedPointEvent = buriedPointEvent;
            return this;
        }

        /**
         * 设置视频比例
         */
        public Builder setScreenScaleType(int screenScaleType) {
            mScreenScaleType = screenScaleType;
            return this;
        }

        /**
         * 自定义RenderView
         */
        public Builder setRenderViewFactory(SurfaceFactory renderViewFactory) {
            mRenderViewFactory = renderViewFactory;
            return this;
        }

        /**
         * 是否适配刘海屏，默认适配
         */
        public Builder setAdaptCutout(boolean adaptCutout) {
            mAdaptCutout = adaptCutout;
            return this;
        }

        /**
         * 是否设置倒计时n秒吐司
         */
        public Builder setIsShowToast(boolean isShowToast) {
            mIsShowToast = isShowToast;
            return this;
        }

        /**
         * 倒计时n秒时间
         */
        public Builder setShowToastTime(long showToastTime) {
            mShowToastTime = showToastTime;
            return this;
        }

        public PlayerConfig build() {
            //创建builder对象
            return new PlayerConfig(this);
        }
    }
}
