package lib.kalu.mediaplayer.core.kernel.video.platfrom.android;

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

import lib.kalu.mediaplayer.core.kernel.video.core.KernelCore;
import lib.kalu.mediaplayer.core.kernel.video.platfrom.PlatfromPlayer;
import lib.kalu.mediaplayer.config.PlayerType;


@Keep
public class AndroidMediaPlayer extends KernelCore implements PlatfromPlayer {

    protected MediaPlayer mMediaPlayer;
    private int mBufferedPercent;
    private boolean mIsPreparing;

    public AndroidMediaPlayer() {
    }

    @NonNull
    @Override
    public AndroidMediaPlayer getPlayer() {
        return this;
    }

    @Override
    public void initKernel(@NonNull Context context) {
        if (null != mMediaPlayer)
            return;
        mMediaPlayer = new MediaPlayer();
        setOptions();
        initListener();
    }

    @Override
    public void resetKernel() {
        if (null == mMediaPlayer)
            return;
        mMediaPlayer.setLooping(false);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
//        mMediaPlayer.setSurface(null);
//        mMediaPlayer.setDisplay(null);
        mMediaPlayer.setVolume(1, 1);
    }

    @Override
    public void releaseKernel() {
        if (null == mMediaPlayer)
            return;
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        mMediaPlayer.release();
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    mMediaPlayer.release();
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
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(onErrorListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        mMediaPlayer.setOnInfoListener(onInfoListener);
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    }

    /**
     * 用于播放raw和asset里面的视频文件
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        try {
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    /**
     * 调整进度
     */
    @Override
    public void seekTo(long time) {
        try {
            mMediaPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public int getPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
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
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
            }
        }
    }

    @Override
    public void prepare(@NonNull Context context, @NonNull String url, @Nullable Map<String, String> headers) {

        //222222222222
        // 设置dataSource
        if (url == null || url.length() == 0) {
            if (getVideoPlayerChangeListener() != null) {
                getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_URL_NULL, 0, getPosition(), getDuration());
            }
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            mMediaPlayer.setDataSource(context, uri, headers);
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_PARSE, e.getMessage());
        }
        try {
            mIsPreparing = true;
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     *
     * @param holder holder
     */
    @Override
    public void setDisplay(SurfaceHolder holder) {
        try {
            mMediaPlayer.setDisplay(holder);
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 设置音量
     *
     * @param v1 v1
     * @param v2 v2
     */
    @Override
    public void setVolume(float v1, float v2) {
        try {
            mMediaPlayer.setVolume(v1, v2);
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 设置是否循环播放
     *
     * @param isLooping 布尔值
     */
    @Override
    public void setLooping(boolean isLooping) {
        try {
            mMediaPlayer.setLooping(isLooping);
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
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
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            } catch (Exception e) {
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
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
                return mMediaPlayer.getPlaybackParams().getSpeed();
            } catch (Exception e) {
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, "监听异常" + what + ", extra: " + extra);
            return true;
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            getVideoPlayerChangeListener().onCompletion();
        }
    };

    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            //解决MEDIA_INFO_VIDEO_RENDERING_START多次回调问题
            if (what == PlayerType.MediaType.MEDIA_INFO_VIDEO_RENDERING_START) {
                if (mIsPreparing) {
                    getVideoPlayerChangeListener().onInfo(what, extra, getPosition(), getDuration());
                    mIsPreparing = false;
                }
            } else {
                getVideoPlayerChangeListener().onInfo(what, extra, getPosition(), getDuration());
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
            long duration = getDuration();
            getVideoPlayerChangeListener().onPrepared(duration);
            start();
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            int videoWidth = mp.getVideoWidth();
            int videoHeight = mp.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0) {
                getVideoPlayerChangeListener().onVideoSizeChanged(videoWidth, videoHeight);
            }
        }
    };
}