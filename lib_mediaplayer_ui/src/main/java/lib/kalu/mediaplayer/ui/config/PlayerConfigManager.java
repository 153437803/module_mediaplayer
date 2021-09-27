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
package lib.kalu.mediaplayer.ui.config;

import android.app.Application;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.LinkedHashMap;

import lib.kalu.mediaplayer.kernel.video.utils.VideoLogUtils;
import lib.kalu.mediaplayer.ui.player.VideoLayout;

/**
 * @description: 视频播放器管理器，管理当前正在播放的VideoView，以及播放器配置
 * * 你也可以用来保存常驻内存的VideoView，但是要注意通过Application Context创建，
 * * 以免内存泄漏
 * @date: 2021-05-12 14:43
 */
@Keep
public class PlayerConfigManager {

    /**
     * 保存VideoView的容器
     */
    private LinkedHashMap<String, VideoLayout> mVideoViews = new LinkedHashMap<>();

    /**
     * 是否在移动网络下直接播放视频
     */
    private boolean mPlayOnMobileNetwork;

    /**
     * VideoViewManager实例
     */
    private static volatile PlayerConfigManager sInstance;

    /**
     * VideoViewConfig实例
     */
    private PlayerConfig mPlayerConfig;

    private PlayerConfigManager() {
        mPlayerConfig = PlayerConfig.newBuilder().build();
        mPlayOnMobileNetwork = mPlayerConfig.mPlayOnMobileNetwork;
    }

    private static class Holder {
        private static final PlayerConfigManager INSTANCE = new PlayerConfigManager();
    }

    public static PlayerConfigManager getInstance() {
        return PlayerConfigManager.Holder.INSTANCE;
    }

    public final void setConfig(@NonNull PlayerConfig config) {
        this.mPlayerConfig = config;
    }

//    /**
//     * 设置VideoViewConfig
//     */
//    public static void setConfig(PlayerConfig config) {
//        if (sConfig == null) {
//            synchronized (PlayerConfig.class) {
//                if (sConfig == null) {
//                    sConfig = config == null ? PlayerConfig.newBuilder().build() : config;
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取VideoViewConfig
//     */
//    public static PlayerConfig getConfig() {
//        setConfig(null);
//        return sConfig;
//    }

    /**
     * 获取VideoViewConfig
     */
    public final PlayerConfig getConfig() {
//        setConfig(null);
        return mPlayerConfig;
    }

    /**
     * 获取是否在移动网络下直接播放视频配置
     */
    public boolean playOnMobileNetwork() {
        return mPlayOnMobileNetwork;
    }

    /**
     * 设置是否在移动网络下直接播放视频
     */
    public void setPlayOnMobileNetwork(boolean playOnMobileNetwork) {
        mPlayOnMobileNetwork = playOnMobileNetwork;
    }

    public static PlayerConfigManager instance() {
        if (sInstance == null) {
            synchronized (PlayerConfigManager.class) {
                if (sInstance == null) {
                    sInstance = new PlayerConfigManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 添加VideoView
     *
     * @param tag 相同tag的VideoView只会保存一个，如果tag相同则会release并移除前一个
     */
    public void add(VideoLayout videoView, String tag) {
        if (!(videoView.getContext() instanceof Application)) {
            VideoLogUtils.i("The Context of this VideoView is not an Application Context," +
                    "you must remove it after release,or it will lead to memory leek.");
        }
        VideoLayout old = get(tag);
        if (old != null) {
            old.release();
            remove(tag);
        }
        mVideoViews.put(tag, videoView);
    }

    public VideoLayout get(String tag) {
        return mVideoViews.get(tag);
    }

    public void remove(String tag) {
        mVideoViews.remove(tag);
    }

    public void removeAll() {
        mVideoViews.clear();
    }

    /**
     * 释放掉和tag关联的VideoView，并将其从VideoViewManager中移除
     */
    public void releaseByTag(String tag) {
        releaseByTag(tag, true);
    }

    public void releaseByTag(String tag, boolean isRemove) {
        VideoLayout videoView = get(tag);
        if (videoView != null) {
            videoView.release();
            if (isRemove) {
                remove(tag);
            }
        }
    }

    public boolean onBackPress(String tag) {
        VideoLayout videoView = get(tag);
        if (videoView == null) {
            return false;
        }
        return videoView.onBackPressed();
    }

}
