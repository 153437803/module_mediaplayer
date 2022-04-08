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
package lib.kalu.mediaplayer.core.view.impl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.Map;

import lib.kalu.mediaplayer.config.PlayerType;
import lib.kalu.mediaplayer.core.controller.base.ControllerLayout;
import lib.kalu.mediaplayer.listener.OnMediaStateListener;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : VideoPlayer抽象接口
 *     revise: 播放器基础属性获取和设置属性接口
 * </pre>
 */
public interface ImplPlayer {

    default void start(@NonNull String url, @NonNull String subtitle) {
        start(0, false, url, subtitle, null);
    }

    default void start(@NonNull String url) {
        start(0, false, url, null, null);
    }

    default void start(@NonNull long seekPosition, @NonNull String url) {
        start(seekPosition, false, url, null, null);
    }

    default void start(@NonNull boolean live, @NonNull String url) {
        start(0, live, url, null, null);
    }

    default void start(@NonNull boolean live, @NonNull String url, @NonNull String subtitle) {
        start(0, live, url, subtitle, null);
    }

    /**
     * 开始播放
     *
     * @param live
     * @param url
     * @param headers
     */
    void start(@NonNull long seekPosition, @NonNull boolean live, @NonNull String url, @NonNull String subtitle, @NonNull Map<String, String> headers);

    void pause();

    void resume();

    void repeat();

    /**
     * 获取视频总时长
     *
     * @return long类型
     */
    long getDuration();


    /**
     * 获取当前播放的位置
     *
     * @return long类型
     */
    long getPosition();

    /**
     * 获取当前缓冲百分比
     *
     * @return 百分比
     */
    int getBufferedPercentage();

    /**
     * 调整播放进度
     *
     * @param pos 位置 毫秒 => System.currentTimeMillis(), getTime()
     */
    void seekTo(long pos);

    /**
     * 是否处于播放状态
     *
     * @return 是否处于播放状态
     */
    boolean isPlaying();

    void startFullScreen();

    void stopFullScreen();

    boolean isFullScreen();

    void setMute(boolean isMute);

    boolean isMute();

    void setScaleType(@PlayerType.ScaleType.Value int scaleType);

    void setSpeed(float speed);

    float getSpeed();

    long getTcpSpeed();

    void setMirrorRotation(boolean enable);

    Bitmap doScreenShot();

    int[] getVideoSize();

    void setRotation(float rotation);

    void startTinyScreen();

    void stopTinyScreen();

    boolean isTinyScreen();

    ControllerLayout getControlLayout();

    View getVideoLayout();

    void initKernel();

    void initRender();

    void resetKernel();

    void releaseKernel();
}
