package com.kalu.mediaplayer;

import lib.kalu.mediaplayer.buried.BuriedPointEvent;
import lib.kalu.mediaplayer.util.MediaLogUtil;

public class BuriedPointEventImpl implements BuriedPointEvent {

    /**
     * 进入视频播放
     *
     * @param url 视频url
     */
    @Override
    public void playerIn(CharSequence url) {
        MediaLogUtil.log("BuriedPointEvent---进入视频播放--" + url);
    }

    
    /**
     * 退出视频播放
     *
     * @param url 视频url
     */
    @Override
    public void playerDestroy(CharSequence url) {
        MediaLogUtil.log("BuriedPointEvent---退出视频播放--" + url);
    }

    /**
     * 视频播放完成
     *
     * @param url 视频url
     */
    @Override
    public void playerCompletion(CharSequence url) {
        MediaLogUtil.log("BuriedPointEvent---视频播放完成--" + url);
    }

    /**
     * 视频播放异常
     *
     * @param url        视频url
     * @param isNetError 是否是网络异常
     */
    @Override
    public void onError(CharSequence url, boolean isNetError) {
        MediaLogUtil.log("BuriedPointEvent---视频播放异常--" + url);
    }

    /**
     * 点击了视频广告
     *
     * @param url 视频url
     */
    @Override
    public void clickAd(CharSequence url) {
        MediaLogUtil.log("BuriedPointEvent---点击了视频广告--" + url);
    }

    /**
     * 视频试看点击
     *
     * @param url 视频url
     */
    @Override
    public void playerAndProved(CharSequence url) {
        MediaLogUtil.log("BuriedPointEvent---视频试看点击--" + url);
    }

    /**
     * 退出视频播放时候的播放进度百度比
     *
     * @param url      视频url
     * @param progress 视频进度，计算百分比【退出时候进度 / 总进度】
     */
    @Override
    public void playerOutProgress(CharSequence url, float progress) {
        MediaLogUtil.log("BuriedPointEvent---退出视频播放时候的播放进度百度比--" + url + "-----" + progress);
    }

    /**
     * 退出视频播放时候的播放进度
     *
     * @param url             视频url
     * @param duration        总时长
     * @param currentPosition 当前进度时长
     */
    @Override
    public void playerOutProgress(CharSequence url, long duration, long currentPosition) {
        MediaLogUtil.log("BuriedPointEvent---退出视频播放时候的播放进度百度比--" + url + "-----" + duration + "----" + currentPosition);
//        VideoLocation location = new VideoLocation(url, currentPosition, duration);
//        CacheConfigManager.getInstance().put(url, location);
    }

    /**
     * 视频切换音频
     *
     * @param url 视频url
     */
    @Override
    public void videoToMusic(CharSequence url) {
        MediaLogUtil.log("BuriedPointEvent---视频切换音频--" + url);
    }
}
