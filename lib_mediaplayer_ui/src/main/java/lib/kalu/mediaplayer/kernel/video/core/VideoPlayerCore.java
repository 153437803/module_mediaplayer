package lib.kalu.mediaplayer.kernel.video.core;

import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.kernel.video.impl.VideoPlayerImpl;
import lib.kalu.mediaplayer.kernel.video.listener.OnVideoPlayerChangeListener;

/**
 * @description:
 * @date: 2021-05-12 09:49
 */
public abstract class VideoPlayerCore implements VideoPlayerImpl {

    /**
     * 播放器事件回调
     */
    @Nullable
    private OnVideoPlayerChangeListener mOnVideoPlayerChangeListener = null;

    @Nullable
    @Override
    public void setOnVideoPlayerChangeListener(@Nullable OnVideoPlayerChangeListener onVideoPlayerChangeListener) {
        this.mOnVideoPlayerChangeListener = onVideoPlayerChangeListener;
    }

    @Nullable
    protected OnVideoPlayerChangeListener getVideoPlayerChangeListener() {
        return mOnVideoPlayerChangeListener;
    }
}