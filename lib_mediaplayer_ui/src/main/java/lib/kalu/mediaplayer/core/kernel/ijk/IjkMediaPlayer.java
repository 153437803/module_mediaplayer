package lib.kalu.mediaplayer.core.kernel.ijk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.core.kernel.KernelApi;
import lib.kalu.mediaplayer.core.kernel.KernelEvent;
import lib.kalu.mediaplayer.util.MediaLogUtil;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

@Keep
public final class IjkMediaPlayer implements KernelApi, KernelEvent {

    private int mBufferedPercent;

    private KernelEvent mEvent;
    private tv.danmaku.ijk.media.player.IjkMediaPlayer mMediaPlayer;

    public IjkMediaPlayer(@NonNull KernelEvent event) {
        this.mEvent = event;
    }

    @NonNull
    @Override
    public IjkMediaPlayer getPlayer() {
        return this;
    }

//    @Override
//    public void createKernel(@NonNull Context context) {
//        // not null
//        if (null != mMediaPlayer) {
//            resetKernel();
//        }
//    }

    @Override
    public void createDecoder(@NonNull Context context) {
        if (null == mMediaPlayer) {
            mMediaPlayer = new tv.danmaku.ijk.media.player.IjkMediaPlayer();
            tv.danmaku.ijk.media.player.IjkMediaPlayer.native_setLogLevel(MediaLogUtil.isIsLog() ? tv.danmaku.ijk.media.player.IjkMediaPlayer.IJK_LOG_INFO : tv.danmaku.ijk.media.player.IjkMediaPlayer.IJK_LOG_SILENT);
            setOptions();
            initListener();
        }
    }

    @Override
    public void releaseDecoder() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void setOptions() {

        int player = tv.danmaku.ijk.media.player.IjkMediaPlayer.OPT_CATEGORY_PLAYER;
        int codec = tv.danmaku.ijk.media.player.IjkMediaPlayer.OPT_CATEGORY_CODEC;
        int format = tv.danmaku.ijk.media.player.IjkMediaPlayer.OPT_CATEGORY_FORMAT;

        //是否有声音, 1无声音、0有声音
        mMediaPlayer.setOption(player, "an", 0);
        mMediaPlayer.setOption(player, "volume", 100);
        //是否有视频, 1无视频、0有视频
        mMediaPlayer.setOption(player, "vn", 0);

        /**
         *  IJK_AVDISCARD_NONE    =-16, ///< discard nothing
         *  IJK_AVDISCARD_DEFAULT =  0, ///< 如果包大小为0，责抛弃无效的包
         *  IJK_AVDISCARD_NONREF  =  8, ///< 抛弃非参考帧（I帧）
         *  IJK_AVDISCARD_BIDIR   = 16, ///< 抛弃B帧
         *  IJK_AVDISCARD_NONKEY  = 32, ///< 抛弃除关键帧以外的，比如B，P帧
         *  IJK_AVDISCARD_ALL     = 48, ///< 抛弃所有的帧
         */
        // 设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        mMediaPlayer.setOption(codec, "skip_loop_filter", 48);
        // 预加载800K
        mMediaPlayer.setOption(format, "probesize", 1024 * 800);
        // 最大缓冲800K
        mMediaPlayer.setOption(player, "max-buffer-size", 1024 * 800);
//
        //设置播放前的最大探测时间
        mMediaPlayer.setOption(format, "analyzeduration", 30 * 1000 * 1000);
        mMediaPlayer.setOption(format, "analyzemaxduration", 30 * 1000 * 1000);
//        //设置是否开启变调isModifyTone?0:1
        mMediaPlayer.setOption(player, "soundtouch", 0);
        //每处理一个packet之后刷新io上下文
        mMediaPlayer.setOption(format, "flush_packets", 1);
        // 关闭播放器缓冲，这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
        mMediaPlayer.setOption(player, "packet-buffering", 0);
        //播放重连次数
        mMediaPlayer.setOption(player, "reconnect", 1);
        // 跳帧处理,放CPU处理较慢时，进行跳帧处理，保证播放流程，画面和声音同步
        mMediaPlayer.setOption(player, "framedrop", 1);
        //最大fps
        mMediaPlayer.setOption(player, "max-fps", 30);

        // SeekTo设置优化
        // 某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        mMediaPlayer.setOption(player, "enable-accurate-seek", 1);
        // 设置seekTo能够快速seek到指定位置并播放
        // 解决m3u8文件拖动问题 比如:一个3个多少小时的音频文件，开始播放几秒中，然后拖动到2小时左右的时间，要loading 10分钟
        mMediaPlayer.setOption(format, "fflags", "fastseek");

        // http、https混合存在时
        mMediaPlayer.setOption(format, "dns_cache_clear", 1);
        //超时时间，timeout参数只对http设置有效，若果你用rtmp设置timeout，ijkplayer内部会忽略timeout参数。rtmp的timeout参数含义和http的不一样。
        mMediaPlayer.setOption(format, "timeout", 30 * 1000 * 1000);

        //rtsp设置 https://ffmpeg.org/ffmpeg-protocols.html#rtsp
        mMediaPlayer.setOption(format, "rtsp_transport", "tcp");
        mMediaPlayer.setOption(format, "rtsp_flags", "prefer_tcp");

        // 根据媒体类型来配置 => bug => resp aac音频无声音
//         mMediaPlayer.setOption(format, "allowed_media_types", "video");
        // 缓冲800K
        mMediaPlayer.setOption(format, "buffer_size", 1024 * 800);
        // 无限读
        mMediaPlayer.setOption(format, "infbuf", 1);

//        mMediaPlayer.setOption(player, "opensles", 0);
//        mMediaPlayer.setOption(player, "overlay-format", tv.danmaku.ijk.media.player.IjkMediaPlayer.SDL_FCC_RV32);
//        mMediaPlayer.setOption(player, "start-on-prepared", 0);
//        mMediaPlayer.setOption(format, "http-detect-range-support", 0);
//
//        //jkPlayer支持硬解码和软解码。
//        //软解码时不会旋转视频角度这时需要你通过onInfo的what == IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED去获取角度，自己旋转画面。
//        //或者开启硬解硬解码，不过硬解码容易造成黑屏无声（硬件兼容问题），下面是设置硬解码相关的代码
        mMediaPlayer.setOption(player, "videotoolbox", 0);
        mMediaPlayer.setOption(player, "mediacodec", 0);//1为硬解 0为软解
        mMediaPlayer.setOption(player, "mediacodec-hevc", 0);//打开h265硬解
        mMediaPlayer.setOption(player, "mediacodec-auto-rotate", 0);
        mMediaPlayer.setOption(player, "mediacodec-handle-resolution-change", 0);
    }

    /**
     * ijk视频播放器监听listener
     */
    private void initListener() {
        // 设置监听，可以查看ijk中的IMediaPlayer源码监听事件
        // 设置视频错误监听器
        mMediaPlayer.setOnErrorListener(onErrorListener);
        // 设置视频播放完成监听事件
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        // 设置视频信息监听器
        mMediaPlayer.setOnInfoListener(onInfoListener);
        // 设置视频缓冲更新监听事件
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        // 设置准备视频播放监听事件
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        // 设置视频大小更改监听器
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        // 设置视频seek完成监听事件
        mMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        // 设置时间文本监听器
        mMediaPlayer.setOnTimedTextListener(onTimedTextListener);
        mMediaPlayer.setOnNativeInvokeListener(new tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener() {
            @Override
            public boolean onNativeInvoke(int i, Bundle bundle) {
                return true;
            }
        });
    }

    /**
     * 用于播放raw和asset里面的视频文件
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        try {
            mMediaPlayer.setDataSource(new RawDataSourceProvider(fd));
        } catch (Exception e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 设置渲染视频的View,主要用于TextureView
     *
     * @param surface surface
     */
    @Override
    public void setSurface(Surface surface) {
        if (surface != null) {
            try {
                mMediaPlayer.setSurface(surface);
            } catch (Exception e) {
                e.printStackTrace();
                mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
            }
        }
    }

    @Override
    public void create(@NonNull Context context, @NonNull long seek, @NonNull long maxLength, @NonNull int maxNum, @NonNull String url) {
        MediaLogUtil.log("K_LOG => init => seek = " + seek + ", maxLength = " + maxLength + ", maxNum = " + maxNum + ", url = " + url);
        update(seek, maxLength, maxNum, url);

        //2222222222222222222222
        // 设置dataSource
        if (url == null || url.length() == 0) {
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_INIT_COMPILE);
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_URL);
            return;
        }
        try {
            //解析path
            Uri uri = Uri.parse(url.toString());
            if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(uri.getScheme())) {
                RawDataSourceProvider rawDataSourceProvider = RawDataSourceProvider.create(context, uri);
                mMediaPlayer.setDataSource(rawDataSourceProvider);
            } else {
                //处理UA问题
//                if (headers != null) {
//                    String userAgent = headers.get("User-Agent");
//                    if (!TextUtils.isEmpty(userAgent)) {
//                        mMediaPlayer.setOption(tv.danmaku.ijk.media.player.IjkMediaPlayer.OPT_CATEGORY_FORMAT, "user_agent", userAgent);
//                    }
//                }
                mMediaPlayer.setDataSource(context, uri, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_PARSE);
        }

        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        try {
            mMediaPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        try {
            return mMediaPlayer.isPlaying();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 调整进度
     */
    @Override
    public void seekTo(long seek) {
        MediaLogUtil.log("IJKLOG => seekTo => seek = " + seek);
        update(seek);
        try {
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_BUFFERING_START);
            mMediaPlayer.seekTo(seek);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getPosition() {
        try {
            return (int) mMediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        try {
            return (int) mMediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float v1, float v2) {
        mMediaPlayer.setVolume(v1, v2);
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        mMediaPlayer.setLooping(isLooping);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        mMediaPlayer.setSpeed(speed);
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        return mMediaPlayer.getSpeed(0);
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        return mMediaPlayer.getTcpSpeed();
    }

    /**
     * 设置视频错误监听器
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频准备渲染
     * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频选择信息
     * int MEDIA_ERROR_SERVER_DIED = 100;//视频中断，一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_IJK_PLAYER = -10000,//一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
     * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
     */
    private IMediaPlayer.OnErrorListener onErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_INIT_COMPILE);
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
            MediaLogUtil.log("IJKLOG => onError => framework_err = " + framework_err + ", impl_err = " + impl_err);
            return true;
        }
    };

    /**
     * 设置视频播放完成监听事件
     */
    private IMediaPlayer.OnCompletionListener onCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_PLAYER_END);
            MediaLogUtil.log("IJKLOG => onCompletion =>");
        }
    };


    /**
     * 设置视频信息监听器
     */
    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {

            // loading-start
            if (what == IMediaPlayer.MEDIA_INFO_OPEN_INPUT) {
                mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_INIT_START);
            }
            // 首帧画面
            else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                mEvent.onEvent(PlayerType.KernelType.IJK, PlayerType.EventType.EVENT_INIT_COMPILE);
                long duration = getDuration();
                mEvent.onEvent(PlayerType.KernelType.IJK, what);
            } else {

                //            long position = getPosition();
                long duration = getDuration();
                mEvent.onEvent(PlayerType.KernelType.IJK, what);
                MediaLogUtil.log("IJKLOG => onInfo => what = " + what + ", extra = " + extra);
            }
            return true;
        }
    };

    /**
     * 设置视频缓冲更新监听事件
     */
    private IMediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
            mBufferedPercent = percent;
        }
    };


    /**
     * 设置准备视频播放监听事件
     */
    private IMediaPlayer.OnPreparedListener onPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            MediaLogUtil.log("K_IJK => onPrepared => seek = " + mSeek);
            long seek = getSeek();
            if (seek > 0) {
                seekTo(seek);
                setSeek(0);
            }
        }
    };

    /**
     * 设置视频大小更改监听器
     */
    private IMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sar_num, int sar_den) {
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHeight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                onChanged(PlayerType.KernelType.IJK, videoWidth, videoHeight, -1);
            }
//            MediaLogUtil.log("IjkVideoPlayer----listener---------onVideoSizeChanged ——> WIDTH：" + width + "， HEIGHT：" + height);
        }
    };

    /**
     * 设置时间文本监听器
     */
    private IMediaPlayer.OnTimedTextListener onTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
        @Override
        public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

        }
    };

    /**
     * 设置视频seek完成监听事件
     */
    private IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {

        }
    };
}
