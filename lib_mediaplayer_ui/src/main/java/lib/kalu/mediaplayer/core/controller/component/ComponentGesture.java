package lib.kalu.mediaplayer.core.controller.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.core.controller.base.ControllerWrapper;
import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.core.controller.impl.GestureApi;
import lib.kalu.mediaplayer.util.PlayerUtils;
import lib.kalu.mediaplayer.util.MediaLogUtil;

public class ComponentGesture extends RelativeLayout implements GestureApi {

    private ControllerWrapper mControllerWrapper;

    public ComponentGesture(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ComponentGesture(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ComponentGesture(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.module_mediaplayer_component_gesture, this, true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setVisibility(INVISIBLE);
        initListener();
    }

    private void initListener() {
    }

    @Override
    public void attach(@NonNull ControllerWrapper controllerWrapper) {
        mControllerWrapper = controllerWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    @Override
    public void onWindowStateChanged(int playerState) {

    }

    /**
     * ????????????
     */
    @Override
    public void onStartSlide() {
        mControllerWrapper.hide();
        View viewRoot = findViewById(R.id.module_mediaplayer_controller_gesture_root);
        viewRoot.setVisibility(VISIBLE);
        viewRoot.setAlpha(1f);
    }

    /**
     * ????????????
     * ?????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    public void onStopSlide() {
        View viewRoot = findViewById(R.id.module_mediaplayer_controller_gesture_root);
        viewRoot.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewRoot.setVisibility(GONE);
                    }
                })
                .start();
    }

    /**
     * ??????????????????
     *
     * @param slidePosition   ????????????
     * @param currentPosition ??????????????????
     * @param duration        ???????????????
     */
    @Override
    public void onPositionChange(int slidePosition, int currentPosition, int duration) {
        MediaLogUtil.log("onPositionChange => slidePosition = " + slidePosition + ", currentPosition = " + currentPosition + ", duration = " + duration);
        TextView viewText = findViewById(R.id.module_mediaplayer_controller_gesture_text);
        if (slidePosition > currentPosition) {
            viewText.setText("??????\n" + String.format("%s/%s", PlayerUtils.formatTime(slidePosition), PlayerUtils.formatTime(duration)));
        } else {
            viewText.setText("??????\n" + String.format("%s/%s", PlayerUtils.formatTime(slidePosition), PlayerUtils.formatTime(duration)));
        }
        ProgressBar viewProgress = findViewById(R.id.module_mediaplayer_controller_gesture_progress);
        viewProgress.setVisibility(GONE);
    }

    /**
     * ??????????????????
     *
     * @param percent ???????????????
     */
    @Override
    public void onBrightnessChange(int percent) {
        TextView viewText = findViewById(R.id.module_mediaplayer_controller_gesture_text);
        viewText.setText("??????\n" + percent + "%");
        ProgressBar viewProgress = findViewById(R.id.module_mediaplayer_controller_gesture_progress);
        viewProgress.setVisibility(VISIBLE);
        viewProgress.setProgress(percent);
    }

    /**
     * ??????????????????
     *
     * @param percent ???????????????
     */
    @Override
    public void onVolumeChange(int percent) {
        TextView viewText = findViewById(R.id.module_mediaplayer_controller_gesture_text);
        if (percent <= 0) {
            viewText.setText("??????\n" + percent + "%");
        } else {
            viewText.setText("??????\n" + percent + "%");
        }
        ProgressBar viewProgress = findViewById(R.id.module_mediaplayer_controller_gesture_progress);
        viewProgress.setVisibility(VISIBLE);
        viewProgress.setProgress(percent);
    }

    @Override
    public void onPlayStateChanged(int playState) {
        if (playState == PlayerType.StateType.STATE_INIT
                || playState == PlayerType.StateType.STATE_START_ABORT
                || playState == PlayerType.StateType.STATE_LOADING_START
                || playState == PlayerType.StateType.STATE_LOADING_STOP
                || playState == PlayerType.StateType.STATE_ERROR
                || playState == PlayerType.StateType.STATE_BUFFERING_START
                || playState == PlayerType.StateType.STATE_ONCE_LIVE) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void onLockStateChanged(boolean isLock) {

    }

}
