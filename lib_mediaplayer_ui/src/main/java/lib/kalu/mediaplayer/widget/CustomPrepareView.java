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
package lib.kalu.mediaplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.controller.ControlWrapper;
import lib.kalu.mediaplayer.config.PlayerConfigManager;
import lib.kalu.mediaplayer.config.PlayerType;

/**
 * 预加载准备播放页面视图
 */
public class CustomPrepareView extends FrameLayout implements ImplController {

    private ControlWrapper mControlWrapper;

    public CustomPrepareView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomPrepareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPrepareView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.module_mediaplayer_video_prepare, this, true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setVisibility(View.VISIBLE);

        findViewById(R.id.tv_start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.fl_net_warning).setVisibility(GONE);
                PlayerConfigManager.instance().setPlayOnMobileNetwork(true);
                mControlWrapper.restart();
            }
        });
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
    }

    @Override
    public void onPlayStateChanged(int playState) {
        View viewLoading = findViewById(R.id.module_mediaplayer_controller_prepare_loading);
        View viewPlay = findViewById(R.id.module_mediaplayer_controller_prepare_play);
        View viewThumb = findViewById(R.id.module_mediaplayer_controller_prepare_image);
        switch (playState) {
            case PlayerType.StateType.STATE_PREPARING:
                bringToFront();
                setVisibility(VISIBLE);
                viewPlay.setVisibility(View.GONE);
                findViewById(R.id.fl_net_warning).setVisibility(GONE);
                viewLoading.setVisibility(View.VISIBLE);
                break;
            case PlayerType.StateType.STATE_PLAYING:
            case PlayerType.StateType.STATE_PAUSED:
            case PlayerType.StateType.STATE_ERROR:
            case PlayerType.StateType.STATE_BUFFERING_PAUSED:
            case PlayerType.StateType.STATE_COMPLETED:
            case PlayerType.StateType.STATE_BUFFERING_PLAYING:
            case PlayerType.StateType.STATE_ONCE_LIVE:
                setVisibility(GONE);
                break;
            case PlayerType.StateType.STATE_IDLE:
                setVisibility(VISIBLE);
                bringToFront();
                viewLoading.setVisibility(View.GONE);
                findViewById(R.id.fl_net_warning).setVisibility(GONE);
                viewPlay.setVisibility(View.VISIBLE);
                viewThumb.setVisibility(View.VISIBLE);
                break;
            case PlayerType.StateType.STATE_START_ABORT:
                setVisibility(VISIBLE);
                findViewById(R.id.fl_net_warning).setVisibility(VISIBLE);
                findViewById(R.id.fl_net_warning).bringToFront();
                break;
        }
    }

    @Override
    public void onWindowStateChanged(int playerState) {
    }

    @Override
    public void setProgress(int duration, int position) {
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
    }
}
