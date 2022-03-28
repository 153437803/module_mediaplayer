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
package lib.kalu.mediaplayer.core.controller.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.config.PlayerConfigManager;
import lib.kalu.mediaplayer.config.PlayerType;
import lib.kalu.mediaplayer.core.controller.base.ControllerWrapper;
import lib.kalu.mediaplayer.core.controller.impl.ImplComponent;
import lib.kalu.mediaplayer.util.MediaLogUtil;
import lib.kalu.mediaplayer.widget.subtitle.widget.SimpleSubtitleView;

public class ComponentSubtitle extends FrameLayout implements ImplComponent {

    private ControllerWrapper mControllerWrapper;

    public ComponentSubtitle(@NonNull Context context) {
        super(context);
        init();
    }

    public ComponentSubtitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ComponentSubtitle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.module_mediaplayer_video_subtitle, this, true);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setVisibility(View.VISIBLE);
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
    public void onPlayStateChanged(int playState) {
        MediaLogUtil.log("ComponentPrepare => onPlayStateChanged => playState = " + playState);
//        switch (playState) {
//            case PlayerType.StateType.STATE_SUBTITLE_START:
//                bringToFront();
//                setVisibility(VISIBLE);
//                SimpleSubtitleView subtitle = findViewById(R.id.module_mediaplayer_controller_subtitle);
//
//                break;
//            default:
//                setVisibility(GONE);
//                break;
//        }
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
