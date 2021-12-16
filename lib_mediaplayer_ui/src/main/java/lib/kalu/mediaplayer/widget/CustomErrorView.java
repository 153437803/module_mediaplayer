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

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.widget.controller.ControlWrapper;
import lib.kalu.mediaplayer.config.PlayerType;
import lib.kalu.mediaplayer.util.PlayerUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 出错提示界面
 *     revise:
 * </pre>
 */
public class CustomErrorView extends LinearLayout implements ImplController {

    private float mDownX;
    private float mDownY;

    private ControlWrapper mControlWrapper;

    public CustomErrorView(Context context) {
        super(context);
        init();
    }

    public CustomErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.module_mediaplayer_video_error, this, true);
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setVisibility(GONE);

        // 重试
        findViewById(R.id.controller_error_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
                mControlWrapper.restart(true);
            }
        });

        // 返回
        findViewById(R.id.controller_error_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击返回键
                if (mControlWrapper.isFullScreen()) {
                    Activity activity = PlayerUtils.scanForActivity(getContext());
                    if (activity != null && !activity.isFinishing()) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mControlWrapper.stopFullScreen();
                    }
                }
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
        if (playState == PlayerType.StateType.STATE_ERROR) {
            bringToFront();
            setVisibility(VISIBLE);

            View view = findViewById(R.id.controller_error_back);
            view.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);

            TextView textView = findViewById(R.id.controller_error_message);
            textView.setText("视频播放异常");
        } else if (playState == PlayerType.StateType.STATE_NETWORK_ERROR) {
            bringToFront();
            setVisibility(VISIBLE);

            View view = findViewById(R.id.controller_error_back);
            view.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);

            TextView textView = findViewById(R.id.controller_error_message);
            textView.setText("无网络，请检查网络设置");
        } else if (playState == PlayerType.StateType.STATE_PARSE_ERROR) {
            bringToFront();
            setVisibility(VISIBLE);

            View view = findViewById(R.id.controller_error_back);
            view.setVisibility(mControlWrapper.isFullScreen() ? VISIBLE : GONE);
            //mTvMessage.setText("视频解析异常");

            TextView textView = findViewById(R.id.controller_error_message);
            textView.setText("视频加载错误");
        } else if (playState == PlayerType.StateType.STATE_IDLE) {
            setVisibility(GONE);
        } else if (playState == PlayerType.StateType.STATE_ONCE_LIVE) {
            setVisibility(GONE);
        }
    }

    @Override
    public void onWindowStateChanged(int playerState) {

    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLock) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float absDeltaX = Math.abs(ev.getX() - mDownX);
                float absDeltaY = Math.abs(ev.getY() - mDownY);
                if (absDeltaX > ViewConfiguration.get(getContext()).getScaledTouchSlop() ||
                        absDeltaY > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
