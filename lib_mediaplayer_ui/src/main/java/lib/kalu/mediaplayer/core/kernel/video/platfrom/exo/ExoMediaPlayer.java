package lib.kalu.mediaplayer.core.kernel.video.platfrom.exo;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Clock;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.video.VideoSize;

import java.util.Map;

import lib.kalu.mediaplayer.config.cache.CacheConfig;
import lib.kalu.mediaplayer.config.cache.CacheConfigManager;
import lib.kalu.mediaplayer.core.kernel.video.core.KernelCore;
import lib.kalu.mediaplayer.core.kernel.video.listener.OnVideoPlayerChangeListener;
import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.util.MediaLogUtil;

@Keep
public class ExoMediaPlayer extends KernelCore implements Player.Listener {

    protected ExoPlayer mExoPlayer;
    //    protected MediaSource mMediaSource;
//    protected ExoMediaSourceHelper mMediaSourceHelper;
    private PlaybackParameters mSpeedPlaybackParameters;
    private int mLastReportedPlaybackState = Player.STATE_IDLE;
    private boolean mLastReportedPlayWhenReady = false;
    private boolean mIsPreparing;
    private boolean mIsBuffering;

//    private LoadControl mLoadControl;
//    private RenderersFactory mRenderersFactory;
//    private TrackSelector mTrackSelector;

    public ExoMediaPlayer() {
    }

    @NonNull
    @Override
    public ExoPlayer getPlayer() {
        return mExoPlayer;
    }

    @Override
    public void initKernel(@NonNull Context context) {
        if (null != mExoPlayer)
            return;
        //创建exo播放器
//        mExoPlayer = new SimpleExoPlayer.Builder(
//                context,
//                mRenderersFactory == null ? mRenderersFactory =  : mRenderersFactory,
//                mTrackSelector == null ? mTrackSelector =  : mTrackSelector,
//               ,
//                )
//                .build();
        ExoPlayer.Builder builder = new ExoPlayer.Builder(context);
        builder.setAnalyticsCollector(new AnalyticsCollector(Clock.DEFAULT));
        builder.setBandwidthMeter(DefaultBandwidthMeter.getSingletonInstance(context));
        builder.setLoadControl(new DefaultLoadControl());
        builder.setMediaSourceFactory(new DefaultMediaSourceFactory(context));
        builder.setTrackSelector(new DefaultTrackSelector(context));
        builder.setRenderersFactory(new DefaultRenderersFactory(context));
        mExoPlayer = builder.build();
        setOptions();
        //播放器日志
        if (mExoPlayer.getTrackSelector() instanceof MappingTrackSelector) {
            mExoPlayer.addAnalyticsListener(new EventLogger((MappingTrackSelector) mExoPlayer.getTrackSelector(), "ExoPlayer"));
        }
        // exo视频播放器监听listener
        mExoPlayer.addListener(this);
    }

    @Override
    public void resetKernel() {
        if (null == mExoPlayer)
            return;

        mExoPlayer.stop();
        mExoPlayer.setVideoSurface(null);
        mIsPreparing = false;
        mIsBuffering = false;
        mLastReportedPlaybackState = Player.STATE_IDLE;
        mLastReportedPlayWhenReady = false;
    }

    @Override
    public void releaseKernel() {
        if (null == mExoPlayer)
            return;
        mExoPlayer.removeListener(this);
//            mExoPlayer.removeVideoListener(this);
        mExoPlayer.release();
        mExoPlayer = null;

        // TODO: 2021-05-21  同步释放，防止卡顿
//            new Thread() {
//                @Override
//                public void run() {
//                    //异步释放，防止卡顿
//                    player.release();
//                }
//            }.start();

        mIsPreparing = false;
        mIsBuffering = false;
        mLastReportedPlaybackState = Player.STATE_IDLE;
        mLastReportedPlayWhenReady = false;
        mSpeedPlaybackParameters = null;
    }

//    public void setTrackSelector(TrackSelector trackSelector) {
//        mTrackSelector = trackSelector;
//    }
//
//    public void setRenderersFactory(RenderersFactory renderersFactory) {
//        mRenderersFactory = renderersFactory;
//    }

//    public void setLoadControl(LoadControl loadControl) {
//        mLoadControl = loadControl;
//    }

//    /**
//     * 设置播放地址
//     *
//     * @param url     播放地址
//     * @param headers 播放地址请求头
//     */
////    @Override
//    public void setDataSource(@NonNull Context context, @NonNull boolean cache, @NonNull String url, @Nullable Map<String, String> headers, @NonNull CacheConfig config) {
//        // 设置dataSource
//        if (url == null || url.length() == 0) {
//            if (getVideoPlayerChangeListener() != null) {
//                getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_URL_NULL, 0);
//            }
//            return;
//        }
//    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        //no support
    }

    /**
     * 播放
     */
    @Override
    public void start() {
        if (mExoPlayer == null) {
            return;
        }
        mExoPlayer.setPlayWhenReady(true);
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        if (mExoPlayer == null) {
            return;
        }
        mExoPlayer.setPlayWhenReady(false);
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        if (mExoPlayer == null) {
            return;
        }
        mExoPlayer.stop();
    }

    private MediaSourceEventListener mMediaSourceEventListener = new MediaSourceEventListener() {

        @Override
        public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
            if (getVideoPlayerChangeListener() != null && mIsPreparing) {
                long position = getPosition();
                long duration = getDuration();
                getVideoPlayerChangeListener().onPrepared(position, duration);
            }
        }
    };

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        if (mExoPlayer == null) {
            return false;
        }
        int state = mExoPlayer.getPlaybackState();
        switch (state) {
            case Player.STATE_BUFFERING:
            case Player.STATE_READY:
                return mExoPlayer.getPlayWhenReady();
            case Player.STATE_IDLE:
            case Player.STATE_ENDED:
            default:
                return false;
        }
    }

    /**
     * 调整进度
     */
    @Override
    public void seekTo(long time) {
        if (mExoPlayer == null) {
            return;
        }
        mExoPlayer.seekTo(time);
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getPosition() {
        if (mExoPlayer == null) {
            return 0L;
        }
        return mExoPlayer.getCurrentPosition();
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        if (mExoPlayer == null) {
            return 0L;
        }
        return  mExoPlayer.getDuration();
    }

    /**
     * 获取缓冲百分比
     */
    @Override
    public int getBufferedPercentage() {
        return mExoPlayer == null ? 0 : mExoPlayer.getBufferedPercentage();
    }

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    @Override
    public void setSurface(Surface surface) {
        if (surface != null) {
            try {
                if (mExoPlayer != null) {
                    mExoPlayer.setVideoSurface(surface);
                }
            } catch (Exception e) {
                if (null != getVideoPlayerChangeListener()) {
                    getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, e.getMessage());
                }
            }
        }
    }

    @Override
    public void prepare(@NonNull Context context, @NonNull CharSequence url, @Nullable Map<String, String> headers) {

        // 222222222222222222222222222
        if (url == null || url.length() == 0) {
            if (getVideoPlayerChangeListener() != null) {
                getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_URL_NULL, 0, getPosition(), getDuration());
            }
            return;
        }

        if (mExoPlayer == null) {
            return;
        }
//        if (mMediaSource == null) {
//            return;
//        }
        if (mSpeedPlaybackParameters != null) {
            mExoPlayer.setPlaybackParameters(mSpeedPlaybackParameters);
        }
        mIsPreparing = true;

        CacheConfig config = CacheConfigManager.getInstance().getCacheConfig();
        MediaSource mediaSource = ExoMediaSourceHelper.getInstance().getMediaSource(context, false, url, headers, config);
        mediaSource.addEventListener(new Handler(), mMediaSourceEventListener);

        //准备播放
        mExoPlayer.setMediaSource(mediaSource);
        mExoPlayer.prepare();
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (holder == null) {
            setSurface(null);
        } else {
            setSurface(holder.getSurface());
        }
    }

    /**
     * 设置音量
     */
    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if (mExoPlayer != null) {
            mExoPlayer.setVolume((leftVolume + rightVolume) / 2);
        }
    }

    /**
     * 设置是否循环播放
     */
    @Override
    public void setLooping(boolean isLooping) {
        if (mExoPlayer != null) {
            mExoPlayer.setRepeatMode(isLooping ? Player.REPEAT_MODE_ALL : Player.REPEAT_MODE_OFF);
        }
    }

    @Override
    public void setOptions() {
        //准备好就开始播放
        mExoPlayer.setPlayWhenReady(true);
    }

    /**
     * 设置播放速度
     */
    @Override
    public void setSpeed(float speed) {
        PlaybackParameters playbackParameters = new PlaybackParameters(speed);
        mSpeedPlaybackParameters = playbackParameters;
        if (mExoPlayer != null) {
            mExoPlayer.setPlaybackParameters(playbackParameters);
        }
    }

    /**
     * 获取播放速度
     */
    @Override
    public float getSpeed() {
        if (mSpeedPlaybackParameters != null) {
            return mSpeedPlaybackParameters.speed;
        }
        return 1f;
    }

    /**
     * 获取当前缓冲的网速
     */
    @Override
    public long getTcpSpeed() {
        // no support
        return 0;
    }

    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, @Player.PlayWhenReadyChangeReason int playbackState) {

        if (getVideoPlayerChangeListener() == null) {
            return;
        }
        if (mIsPreparing) {
            return;
        }
        if (mLastReportedPlayWhenReady != playWhenReady || mLastReportedPlaybackState != playbackState) {
            switch (playbackState) {
                //最开始调用的状态
                case Player.STATE_IDLE:
                    break;
                //开始缓充
                case Player.STATE_BUFFERING:
                    getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_BUFFERING_START, getBufferedPercentage(), getPosition(), getDuration());
                    mIsBuffering = true;
                    break;
                //开始播放
                case Player.STATE_READY:

                    MappingTrackSelector trackSelector = (MappingTrackSelector) mExoPlayer.getTrackSelector();
                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                    if (mappedTrackInfo != null) {
                        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
                            TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(i);
                            if (C.TRACK_TYPE_AUDIO == mappedTrackInfo.getRendererType(i)) { //判断是否是音轨
                                for (int groupIndex = 0; groupIndex < rendererTrackGroups.length; groupIndex++) {
                                    TrackGroup trackGroup = rendererTrackGroups.get(groupIndex);
                                    MediaLogUtil.log("SRT => checkAudio => " + trackGroup.getFormat(0).toString());
                                }
                            } else if (C.TRACK_TYPE_TEXT == mappedTrackInfo.getRendererType(i)) { //判断是否是字幕
                                for (int groupIndex = 0; groupIndex < rendererTrackGroups.length; groupIndex++) {
                                    TrackGroup trackGroup = rendererTrackGroups.get(groupIndex);
                                    MediaLogUtil.log("SRT => checkSubTitle => " + trackGroup.getFormat(0).toString());
                                }
                            }
                        }
                    }

                    if (mIsBuffering) {
                        getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_BUFFERING_END, getBufferedPercentage(), getPosition(), getDuration());
                        mIsBuffering = false;
                    }
                    break;
                //播放器已经播放完了媒体
                case Player.STATE_ENDED:
                    getVideoPlayerChangeListener().onCompletion();
                    break;
                default:
                    break;
            }
            mLastReportedPlaybackState = playbackState;
            mLastReportedPlayWhenReady = playWhenReady;
        }
    }

//    @Override
//    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//    }

    @Override
    public void onPlayerError(PlaybackException error) {

        if (null == error || !(error instanceof ExoPlaybackException))
            return;

        OnVideoPlayerChangeListener listener = getVideoPlayerChangeListener();
        if (null == listener)
            return;

        MediaLogUtil.log("onPlayerError => type = " + ((ExoPlaybackException) error).type + ", error = " + error);
        switch (((ExoPlaybackException) error).type) {
            case PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW:
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_SOURCE, error.getMessage());
                break;
            case ExoPlaybackException.TYPE_RENDERER:
            case ExoPlaybackException.TYPE_UNEXPECTED:
            case ExoPlaybackException.TYPE_REMOTE:
//            case ExoPlaybackException.TYPE_SOURCE:
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_UNEXPECTED, error.getMessage());
                break;
            default:
                getVideoPlayerChangeListener().onError(PlayerType.ErrorType.ERROR_RETRY, error.getMessage());
                break;
        }
    }

    @Override
    public void onPlayerErrorChanged(@Nullable PlaybackException error) {
//        String errorCodeName = error.getErrorCodeName();
//        MediaLogUtil.log("onPlayerErrorChanged => errorCodeName = " + errorCodeName);
    }

    //    public void onPlayerError(PlaybackException error) {
//
//        String errorCodeName = error.getErrorCodeName();
//        MediaLogUtil.log("onPlayerError => errorCodeName = " + errorCodeName);
//
//    }

    @Override
    public void onVideoSizeChanged(VideoSize videoSize) {
        if (getVideoPlayerChangeListener() != null) {
            getVideoPlayerChangeListener().onSize(videoSize.width, videoSize.height);
            if (videoSize.unappliedRotationDegrees > 0) {
                getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_VIDEO_ROTATION_CHANGED, videoSize.unappliedRotationDegrees, getPosition(), getDuration());
            }
        }
    }

    @Override
    public void onRenderedFirstFrame() {
        if (getVideoPlayerChangeListener() != null && mIsPreparing) {
            getVideoPlayerChangeListener().onInfo(PlayerType.MediaType.MEDIA_INFO_VIDEO_RENDERING_START, 0, getPosition(), getDuration());
            mIsPreparing = false;
        }
    }
}
