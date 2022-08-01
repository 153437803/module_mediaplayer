package lib.kalu.mediaplayer.core.kernel.vlc;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.interfaces.IVLCVout;

import java.util.Map;

import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.core.kernel.KernelApi;
import lib.kalu.mediaplayer.core.kernel.KernelEvent;
import lib.kalu.mediaplayer.util.MediaLogUtil;

@Keep
public final class VlcMediaPlayer implements KernelApi, KernelEvent {

    //    private LibVLC mLibVLC;
    private KernelEvent mEvent;
    private org.videolan.libvlc.media.MediaPlayer mVlcPlayer;

    public VlcMediaPlayer(@NonNull KernelEvent event) {
        this.mEvent = event;
    }

    @NonNull
    @Override
    public VlcMediaPlayer getPlayer() {
        return this;
    }

    @Override
    public void createDecoder(@NonNull Context context) {
        //        ArrayList args = new ArrayList<>();//VLC参数
//        args.add("--rtsp-tcp");//强制rtsp-tcp，加快加载视频速度
//        args.add("--aout=opensles");
//        args.add("--audio-time-stretch");
//        args.add("-vvv");
//        mLibVLC = new LibVLC(context);
        mVlcPlayer = new org.videolan.libvlc.media.MediaPlayer(context);
        setOptions();
        initListener();
    }

    @Override
    public void releaseDecoder() {
        releaseMusic();
        if (null != mVlcPlayer) {
            mVlcPlayer.release();
            mVlcPlayer = null;
        }
    }

    /**
     * MediaPlayer视频播放器监听listener
     */
    private void initListener() {
//        mVlcPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mVlcPlayer.setOnErrorListener(onErrorListener);
//        mVlcPlayer.setOnCompletionListener(onCompletionListener);
//        mVlcPlayer.setOnInfoListener(onInfoListener);
//        mVlcPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
//        mVlcPlayer.setOnPreparedListener(onPreparedListener);
//        mVlcPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        mVlcPlayer.getVLC().setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                MediaLogUtil.log("K_VLC => event = " + event.type);
                // 首帧画面
                if (event.type == MediaPlayer.Event.Vout) {
                    mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_INIT_COMPILE);
                    mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_VIDEO_START);

                    long seek = getSeek();
                    if (seek > 0) {
                        seekTo(seek);
                    }
                }
                // 解析开始
                else if (event.type == MediaPlayer.Event.MediaChanged) {
                    mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_INIT_START);
                }
                // 播放完成
                else if (event.type == MediaPlayer.Event.EndReached) {
                    mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_PLAYER_END);
                }
                // 错误
                else if (event.type == MediaPlayer.Event.Stopped) {
                    mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_INIT_COMPILE);
                    mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_PARSE);
                }
            }
        });
    }

    /**
     * 用于播放raw和asset里面的视频文件
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        try {
            mVlcPlayer.setDataSource(fd.getFileDescriptor());
        } catch (Exception e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        try {
            mVlcPlayer.start();
        } catch (IllegalStateException e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        try {
            mVlcPlayer.pause();
        } catch (IllegalStateException e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        try {
            mVlcPlayer.stop();
        } catch (IllegalStateException e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        try {
            return mVlcPlayer.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 调整进度
     */
    @Override
    public void seekTo(long seek) {
        try {
            mVlcPlayer.seekTo(seek);
        } catch (IllegalStateException e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getPosition() {
        return mVlcPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        return mVlcPlayer.getDuration();
    }

    /**
     * 获取缓冲百分比
     *
     * @return 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
//        return mBufferedPercent;
        return 0;
    }

    @Override
    public void init(@NonNull Context context, @NonNull long seek, @NonNull long max, @NonNull String url) {

        // 设置dataSource
        if (url == null || url.length() == 0) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_INIT_COMPILE);
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_URL);
            return;
        }
        try {
            mVlcPlayer.setDataSource(url);//

        } catch (Exception e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_PARSE);
        }
        try {
            start();
        } catch (IllegalStateException e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    @Override
    public void setSurface(@NonNull Surface surface) {
        if (null != surface && null != mVlcPlayer) {
            try {
                mVlcPlayer.setSurface(surface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void setReal(@NonNull Surface surface, @NonNull SurfaceHolder holder) {
//
//        // 设置渲染视频的View,主要用于SurfaceView
//        if (null != holder && null != mVlcPlayer) {
//            try {
//                mVlcPlayer.setDisplay(holder);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

    /**
     * 设置音量
     *
     * @param v1 v1
     * @param v2 v2
     */
    @Override
    public void setVolume(float v1, float v2) {
        KernelApi.super.setVolume(v1, v2);
        try {
            mVlcPlayer.setVolume(v1, v2);
        } catch (Exception e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
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
            mVlcPlayer.setLooping(isLooping);
        } catch (Exception e) {
            mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
        }
    }

    @Override
    public boolean isLooping() {
        try {
            return mVlcPlayer.isLooping();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
                mVlcPlayer.setSpeed(speed);
            } catch (Exception e) {
                mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
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
                return mVlcPlayer.getSpeed();
            } catch (Exception e) {
                mEvent.onEvent(PlayerType.KernelType.VLC, PlayerType.EventType.EVENT_ERROR_UNEXPECTED);
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
}
