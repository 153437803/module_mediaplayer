/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package lib.kalu.mediaplayer.kernel.video.platfrom.media;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Map;

import lib.kalu.mediaplayer.common.contentprovider.ContentProviderMediaplayer;
import lib.kalu.mediaplayer.kernel.video.core.VideoPlayerCore;
import lib.kalu.mediaplayer.kernel.video.platfrom.PlatfromPlayer;
import lib.kalu.mediaplayer.ui.config.PlayerType;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 不推荐，系统的MediaPlayer兼容性较差，建议使用IjkPlayer或者ExoPlayer
 *     revise:
 * </pre>
 */
@Keep
public class AndroidMediaPlayer extends VideoPlayerCore implements PlatfromPlayer {

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
    public void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        setOptions();
        initListener();
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
     * 设置播放地址
     *
     * @param path    播放地址
     * @param headers 播放地址请求头
     */
    @Override
    public void setDataSource(String path, Map<String, String> headers, boolean isCahche) {
        // 设置dataSource
        if (path == null || path.length() == 0) {
            if (getVideoPlayerChangeListener() != null) {
                getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_URL_NULL, 0);
            }
            return;
        }
        try {
            Uri uri = Uri.parse(path);
            mMediaPlayer.setDataSource(ContentProviderMediaplayer.getContextWeakReference(), uri, headers);
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_PARSE, e.getMessage());
        }
    }

    /**
     * 用于播放raw和asset里面的视频文件
     */
    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        try {
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        } catch (Exception e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
        }
    }

    @Override
    public void prepareAsync() {
        try {
            mIsPreparing = true;
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 重置播放器
     */
    @Override
    public void reset() {
        mMediaPlayer.reset();
        mMediaPlayer.setSurface(null);
        mMediaPlayer.setDisplay(null);
        mMediaPlayer.setVolume(1, 1);
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
        }
    }

    /**
     * 释放播放器
     */
    @Override
    public void release() {
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        new Thread() {
            @Override
            public void run() {
                try {
                    mMediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
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
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
            }
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, e.getMessage());
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
            getVideoPlayerChangeListener().onError(PlayerType.ErrorType.TYPE_UNEXPECTED, "监听异常" + what + ", extra: " + extra);
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
                    getVideoPlayerChangeListener().onInfo(what, extra);
                    mIsPreparing = false;
                }
            } else {
                getVideoPlayerChangeListener().onInfo(what, extra);
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
            getVideoPlayerChangeListener().onPrepared();
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
