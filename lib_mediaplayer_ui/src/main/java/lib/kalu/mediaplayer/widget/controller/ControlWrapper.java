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
package lib.kalu.mediaplayer.widget.controller;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import lib.kalu.mediaplayer.widget.controller.ImplController;
import lib.kalu.mediaplayer.widget.player.InterVideoPlayer;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 此类的目的是为了在InterControlView接口实现类中既能调用VideoPlayer的api又能调用BaseVideoController的api，
 *             并对部分api做了封装，方便使用
 *     revise:
 * </pre>
 */
@Keep
public class ControlWrapper implements InterVideoPlayer, ImplController {

    private InterVideoPlayer mVideoPlayer;
    private ImplController mController;

    public ControlWrapper(@NonNull InterVideoPlayer videoPlayer, @NonNull ImplController controller) {
        mVideoPlayer = videoPlayer;
        mController = controller;
    }

//    @Override
//    public void setUrl(String url) {
//        mVideoPlayer.setUrl(url);
//    }
//
//    @Override
//    public void setUrl(@NonNull boolean cache, @NonNull String url) {
//        mVideoPlayer.setUrl(cache, url);
//    }
//
//    @Override
//    public String getUrl() {
//        return mVideoPlayer.getUrl();
//    }

    @Override
    public void start(@NonNull boolean cache, @NonNull String url, @NonNull Map<String, String> headers) {
        mVideoPlayer.start(cache, url, headers);
    }

    @Override
    public void restart(@NonNull boolean reset) {
        mVideoPlayer.restart(reset);
    }

    @Override
    public void pause() {
        mVideoPlayer.pause();
    }

    @Override
    public long getDuration() {
        return mVideoPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mVideoPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(long pos) {
        mVideoPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mVideoPlayer.isPlaying();
    }

    @Override
    public int getBufferedPercentage() {
        return mVideoPlayer.getBufferedPercentage();
    }

    @Override
    public void startFullScreen() {
        mVideoPlayer.startFullScreen();
    }

    @Override
    public void stopFullScreen() {
        mVideoPlayer.stopFullScreen();
    }

    @Override
    public boolean isFullScreen() {
        return mVideoPlayer.isFullScreen();
    }

    @Override
    public void setMute(boolean isMute) {
        mVideoPlayer.setMute(isMute);
    }

    @Override
    public boolean isMute() {
        return mVideoPlayer.isMute();
    }

    @Override
    public void setScreenScaleType(int screenScaleType) {
        mVideoPlayer.setScreenScaleType(screenScaleType);
    }

    @Override
    public void setSpeed(float speed) {
        mVideoPlayer.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return mVideoPlayer.getSpeed();
    }

    @Override
    public long getTcpSpeed() {
        return mVideoPlayer.getTcpSpeed();
    }

    @Override
    public void setMirrorRotation(boolean enable) {
        mVideoPlayer.setMirrorRotation(enable);
    }

    @Override
    public Bitmap doScreenShot() {
        return mVideoPlayer.doScreenShot();
    }

    @Override
    public int[] getVideoSize() {
        return mVideoPlayer.getVideoSize();
    }

    @Override
    public void setRotation(float rotation) {
        mVideoPlayer.setRotation(rotation);
    }

    @Override
    public void startTinyScreen() {
        mVideoPlayer.startTinyScreen();
    }

    @Override
    public void stopTinyScreen() {
        mVideoPlayer.stopTinyScreen();
    }

    @Override
    public boolean isTinyScreen() {
        return mVideoPlayer.isTinyScreen();
    }

    /**
     * 播放和暂停
     */
    public void toggle() {
        if (isPlaying()) {
            pause();
        } else {
            restart();
        }
    }

    /**
     * 横竖屏切换，会旋转屏幕
     */
    public void toggleFullScreen(Activity activity) {
        if (activity == null || activity.isFinishing())
            return;
        if (isFullScreen()) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            stopFullScreen();
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            startFullScreen();
        }
    }

    /**
     * 横竖屏切换，不会旋转屏幕
     */
    public void toggleFullScreen() {
        if (isFullScreen()) {
            stopFullScreen();
        } else {
            startFullScreen();
        }
    }

    /**
     * 横竖屏切换，根据适配宽高决定是否旋转屏幕
     */
    public void toggleFullScreenByVideoSize(Activity activity) {
        if (activity == null || activity.isFinishing())
            return;
        int[] size = getVideoSize();
        int width = size[0];
        int height = size[1];
        if (isFullScreen()) {
            stopFullScreen();
            if (width > height) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            startFullScreen();
            if (width > height) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    @Override
    public void startFadeOut() {
        mController.startFadeOut();
    }

    @Override
    public void stopFadeOut() {
        mController.stopFadeOut();
    }

    @Override
    public boolean isShowing() {
        return mController.isShowing();
    }

    @Override
    public void setLocked(boolean locked) {
        mController.setLocked(locked);
    }

    @Override
    public boolean isLocked() {
        return mController.isLocked();
    }

    @Override
    public void startProgress() {
        mController.startProgress();
    }

    @Override
    public void stopProgress() {
        mController.stopProgress();
    }

    @Override
    public void hide() {
        mController.hide();
    }

    @Override
    public void show() {
        mController.show();
    }

    @Override
    public boolean hasCutout() {
        return mController.hasCutout();
    }

    @Override
    public int getCutoutHeight() {
        return mController.getCutoutHeight();
    }

    @Nullable
    @Override
    public ImageView getPrepare() {
        return null;
    }

    @Override
    public void destroy() {
        mController.destroy();
    }

    @Override
    public void init() {

    }

    @Override
    public int initLayout() {
        return 0;
    }

    /**
     * 切换锁定状态
     */
    public void toggleLockState() {
        setLocked(!isLocked());
    }


    /**
     * 切换显示/隐藏状态
     */
    public void toggleShowState() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }
}