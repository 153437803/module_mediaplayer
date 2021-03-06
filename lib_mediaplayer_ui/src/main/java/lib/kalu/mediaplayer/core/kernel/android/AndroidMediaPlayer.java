package lib.kalu.mediaplayer.core.kernel.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import lib.kalu.mediaplayer.core.kernel.KernelApi;
import lib.kalu.mediaplayer.core.kernel.KernelEvent;
import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.util.MediaLogUtil;

@Keep
public final class AndroidMediaPlayer implements KernelApi {

    private long mSeek = 0L; // 快进
    private long mMax = 0L; // 试播时常
    private boolean mLoop = false; // 循环播放
    private boolean mMute = false; // 静音
    private String mUrl = null; // 视频串
    private android.media.MediaPlayer mMusicPlayer = null; // 配音音频

    private KernelEvent mEvent;
    private MediaPlayer mAndroidPlayer;

    private int mBufferedPercent;

    public AndroidMediaPlayer(@NonNull KernelEvent event) {
        this.mEvent = event;
    }

    @NonNull
    @Override
    public AndroidMediaPlayer getPlayer() {
        return this;
    }

    @Override
    public void createDecoder(@NonNull Context context) {
        releaseDecoder();
        mAndroidPlayer = new MediaPlayer();
        mAndroidPlayer.setLooping(false);
        setOptions();
        initListener();
    }

    @Override
    public void releaseDecoder() {
        releaseMusic();
        if (null != mAndroidPlayer) {
            mAndroidPlayer.setOnErrorListener(null);
            mAndroidPlayer.setOnCompletionListener(null);
            mAndroidPlayer.setOnInfoListener(null);
            mAndroidPlayer.setOnBufferingUpdateListener(null);
            mAndroidPlayer.setOnPreparedListener(null);
            mAndroidPlayer.setOnVideoSizeChangedListener(null);
            mAndroidPlayer.setLooping(false);
            mAndroidPlayer.stop();
            mAndroidPlayer.reset();
            mAndroidPlayer.release();
            mAndroidPlayer = null;
        }

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    mAndroidPlayer.release();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }

    /**
     * MediaPlayer视频播放器监听listener
     */
    private void initListener() {
        mAndroidPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mAndroidPlayer.setOnErrorListener(onErrorListener);
        mAndroidPlayer.setOnCompletionListener(onCompletionListener);
        mAndroidPlayer.setOnInfoListener(onInfoListener);
        mAndroidPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mAndroidPlayer.setOnPreparedListener(onPreparedListener);
        mAndroidPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    }

    /**
     * 用于播放raw和asset里面的视频文件
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        try {
            mAndroidPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        try {
            mAndroidPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        try {
            mAndroidPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        try {
            mAndroidPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        return mAndroidPlayer.isPlaying();
    }

    /**
     * 调整进度
     */
    @Override
    public void seekTo(long time) {
        try {
            mAndroidPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getPosition() {
        return mAndroidPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        return mAndroidPlayer.getDuration();
    }

    /**
     * 获取缓冲百分比
     *
     * @return 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
    }

    @Override
    public void init(@NonNull Context context, @NonNull long seek, @NonNull long max, @NonNull String url) {

        // loading-start
        mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_INIT_START);

        // 设置dataSource
        if (url == null || url.length() == 0) {
            mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_INIT_COMPILE);
            mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_ERROR_URL);
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            mAndroidPlayer.setDataSource(context, uri, null);
        } catch (Exception e) {
            mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_ERROR_PARSE);
        }
        try {
            mAndroidPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSurface(@NonNull Surface surface) {
        if (null != surface && null != mAndroidPlayer) {
            try {
                mAndroidPlayer.setSurface(surface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setOptions() {
    }

    /**
     * 设置播放速度
     *
     * @param speed 速度
     */
    @Override
    public void setSpeed(float speed) {
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mAndroidPlayer.setPlaybackParams(mAndroidPlayer.getPlaybackParams().setSpeed(speed));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取播放速度
     *
     * @return 播放速度
     */
    @Override
    public float getSpeed() {
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return mAndroidPlayer.getPlaybackParams().getSpeed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1f;
    }

    /**
     * 获取当前缓冲的网速
     *
     * @return 获取网络
     */
    @Override
    public long getTcpSpeed() {
        // no support
        return 0;
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            MediaLogUtil.log("K_ANDROID => onError => what = " + what);
            // ignore -38
            if (what == -38) {

            }
            // ignore 1
            else if (what == 1) {
//                resetKernel();
                mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_INIT_COMPILE);
                mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_ERROR_PARSE);
            }
            // next
            else {
//                resetKernel();
                mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_INIT_COMPILE);
            }
            return true;
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            MediaLogUtil.log("K_ANDROID => onCompletion => ");
            mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_PLAYER_END);
        }
    };

    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            MediaLogUtil.log("K_ANDROID => onInfo => what = " + what);
            //解决MEDIA_INFO_VIDEO_RENDERING_START多次回调问题
//            MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START
            if (what == PlayerType.EventType.EVENT_VIDEO_START) {
//                if (mIsPreparing) {
//                    mIsPreparing = false;
//                }
            } else {
                mEvent.onEvent(PlayerType.KernelType.ANDROID, what);
            }
            return true;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mBufferedPercent = percent;
        }
    };


    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            MediaLogUtil.log("K_ANDROID => onPrepared => ");

            mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_INIT_COMPILE);
//            int position = mp.getCurrentPosition();
//            long duration = getDuration();
//            getVideoPlayerChangeListener().onPrepared(mSeek, duration);

            start();
            long seek = getSeek();
            if (seek > 0) {
                seekTo(seek);
            }

            mEvent.onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_VIDEO_START);
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                onChanged(PlayerType.KernelType.ANDROID, videoWidth, videoHeight, -1);
            }
        }
    };

    /****************/

    @Override
    public void setVolume(float v1, float v2) {
        mMute = (v1 <= 0 || v2 <= 0);
        try {
            mAndroidPlayer.setVolume(v1, v2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isMute() {
        return mMute;
    }

    @Override
    public void setMusicPlayer(@NonNull android.media.MediaPlayer player) {
        this.mMusicPlayer = player;
    }

    @Override
    public android.media.MediaPlayer getMusicPlayer() {
        return mMusicPlayer;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public long getSeek() {
        return mSeek;
    }

    @Override
    public void setSeek(long seek) {
        if (seek < 0)
            return;
        mSeek = seek;
    }

    @Override
    public long getMax() {
        return mMax;
    }

    @Override
    public void setMax(long max) {
        if (max < 0)
            return;
        mMax = max;
    }

    @Override
    public void setLooping(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public boolean isLooping() {
        return mLoop;
    }

    /****************/
}
