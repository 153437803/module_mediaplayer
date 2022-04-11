package lib.kalu.mediaplayer.core.controller.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Map;

import lib.kalu.mediaplayer.core.controller.impl.ImplController;
import lib.kalu.mediaplayer.core.view.ImplPlayer;

@Keep
public class ControllerWrapper implements ImplPlayer, ImplController {

    // 播放器
    private ImplPlayer mPlayer;
    // 控制器
    private ImplController mController;

    public ControllerWrapper(@NonNull ImplPlayer player, @NonNull ImplController controller) {
        this.mPlayer = player;
        this.mController = controller;
    }

    @Override
    public void start(@NonNull long seekPosition, @NonNull boolean live, @NonNull String url, @NonNull String subtitle, @NonNull Map<String, String> headers) {
        mPlayer.start(seekPosition, live, url, subtitle, headers);
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void resume() {
        mPlayer.resume();
    }

    @Override
    public void repeat() {
        mPlayer.repeat();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public int getPosition() {
        return mPlayer.getPosition();
    }

    @Override
    public void seekTo(long pos) {
        mPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getBufferedPercentage() {
        return mPlayer.getBufferedPercentage();
    }

    @Override
    public void startFullScreen() {
        mPlayer.startFullScreen();
    }

    @Override
    public void stopFullScreen() {
        mPlayer.stopFullScreen();
    }

    @Override
    public boolean isFullScreen() {
        return mPlayer.isFullScreen();
    }

    @Override
    public void setMute(boolean isMute) {
        mPlayer.setMute(isMute);
    }

    @Override
    public boolean isMute() {
        return mPlayer.isMute();
    }

    @Override
    public void setScaleType(int scaleType) {
        mPlayer.setScaleType(scaleType);
    }

    @Override
    public void setSpeed(float speed) {
        mPlayer.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return mPlayer.getSpeed();
    }

    @Override
    public long getTcpSpeed() {
        return mPlayer.getTcpSpeed();
    }

    @Override
    public void setMirrorRotation(boolean enable) {
        mPlayer.setMirrorRotation(enable);
    }

    @Override
    public Bitmap doScreenShot() {
        return mPlayer.doScreenShot();
    }

    @Override
    public int[] getVideoSize() {
        return mPlayer.getVideoSize();
    }

    @Override
    public void setRotation(float rotation) {
        mPlayer.setRotation(rotation);
    }

    @Override
    public void startTinyScreen() {
        mPlayer.startTinyScreen();
    }

    @Override
    public void stopTinyScreen() {
        mPlayer.stopTinyScreen();
    }

    @Override
    public boolean isTinyScreen() {
        return mPlayer.isTinyScreen();
    }

    @Override
    public ControllerLayout getControlLayout() {
        return null;
    }

    @Override
    public View getVideoLayout() {
        return null;
    }

    @Override
    public void initKernel() {
        mPlayer.initKernel();
    }

    @Override
    public void initRender() {
        mPlayer.initRender();
    }

//    @Override
//    public void resetKernel() {
//        mPlayer.resetKernel();
//    }

    @Override
    public void releaseKernel() {
        mPlayer.releaseKernel();
    }

    /**
     * 播放和暂停
     */
    public void toggle() {
        if (isPlaying()) {
            pause();
        } else {
            resume();
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
