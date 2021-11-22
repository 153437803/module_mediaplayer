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
package lib.kalu.mediaplayer.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.ui.config.PlayerType;
import lib.kalu.mediaplayer.ui.controller.ControllerLayoutDispatchTouchEvent;
import lib.kalu.mediaplayer.ui.tool.BaseToast;
import lib.kalu.mediaplayer.ui.tool.PlayerUtils;


/**
 * description: 控制器 - mobile
 * 如果想定制ui，你可以直接继承GestureVideoController或者BaseVideoController实现
 * created by kalu on 2021/9/16
 */
@Keep
public class DefaultController extends ControllerLayoutDispatchTouchEvent implements View.OnClickListener {

    private ImageView mLockButton;
    private ProgressBar mLoadingProgress;
    private ImageView thumb;
    private CustomTopView titleView;
    private CustomBottomView vodControlView;
    private CustomLiveControlView liveControlView;
    private CustomOncePlayView customOncePlayView;
    private TextView tvLiveWaitMessage;
    /**
     * 是否是直播，默认不是
     */
    public static boolean IS_LIVE = false;

    public DefaultController(@NonNull Context context) {
        this(context, null);
    }

    public DefaultController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int initLayout() {
        return R.layout.module_mediaplayer_video_standard;
    }

    @Override
    public void init() {
        super.init();
        initFindViewById();
        initListener();
        initConfig();
    }

    private void initFindViewById() {
        mLockButton = findViewById(R.id.lock);
        mLoadingProgress = findViewById(R.id.loading);
    }

    private void initListener() {
        mLockButton.setOnClickListener(this);
    }

    private void initConfig() {
        //根据屏幕方向自动进入/退出全屏
        setEnableOrientation(true);
        //设置可以滑动调节进度
        setCanChangePosition(true);
        //竖屏也开启手势操作，默认关闭
        setEnableInNormal(true);
        //滑动调节亮度，音量，进度，默认开启
        setGestureEnabled(true);
        //先移除多有的视图view
        removeAllControlComponent();
        //添加视图到界面
        addDefaultControlComponent("");
    }



    /**
     * 快速添加各个组件
     * 需要注意各个层级
     * @param title                             标题
     */
    public void addDefaultControlComponent(String title) {
        //添加自动完成播放界面view
        CustomCompleteView completeView = new CustomCompleteView(getContext());
        completeView.setVisibility(GONE);
        this.addControlComponent(completeView);

        //添加错误界面view
        CustomErrorView errorView = new CustomErrorView(getContext());
        errorView.setVisibility(GONE);
        this.addControlComponent(errorView);

        //添加与加载视图界面view，准备播放界面
        CustomPrepareView prepareView = new CustomPrepareView(getContext());
        thumb = prepareView.getThumb();
        prepareView.setClickStart();
        this.addControlComponent(prepareView);

        //添加标题栏
        titleView = new CustomTopView(getContext());
        titleView.setTitle(title);
        titleView.setVisibility(VISIBLE);
        this.addControlComponent(titleView);

        //添加直播/回放视频底部控制视图
        changePlayType();

        //添加滑动控制视图
        CustomGestureView gestureControlView = new CustomGestureView(getContext());
        this.addControlComponent(gestureControlView);
    }


    /**
     * 切换直播/回放类型
     */
    public void changePlayType(){
        if (IS_LIVE) {
            //添加底部播放控制条
            if (liveControlView==null){
                liveControlView = new CustomLiveControlView(getContext());
            }
            this.removeControlComponent(liveControlView);
            this.addControlComponent(liveControlView);

            //添加直播还未开始视图
            if (customOncePlayView==null){
                customOncePlayView = new CustomOncePlayView(getContext());
                tvLiveWaitMessage = customOncePlayView.getTvMessage();
            }
            this.removeControlComponent(customOncePlayView);
            this.addControlComponent(customOncePlayView);

            //直播视频，移除回放视图
            if (vodControlView!=null){
                this.removeControlComponent(vodControlView);
            }
        } else {
            //添加底部播放控制条
            if (vodControlView==null){
                vodControlView = new CustomBottomView(getContext());
                //是否显示底部进度条。默认显示
                vodControlView.showBottomProgress(true);
            }
            this.removeControlComponent(vodControlView);
            this.addControlComponent(vodControlView);

            //正常视频，移除直播视图
            if (liveControlView!=null){
                this.removeControlComponent(liveControlView);
            }
            if (customOncePlayView!=null){
                this.removeControlComponent(customOncePlayView);
            }
        }
        setCanChangePosition(!IS_LIVE);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lock) {
            mControlWrapper.toggleLockState();
        }
    }

    @Override
    protected void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            mLockButton.setSelected(true);
            String string = getContext().getResources().getString(R.string.module_mediaplayer_string_locked);
            BaseToast.showRoundRectToast(getContext(), string);
        } else {
            mLockButton.setSelected(false);
            String string = getContext().getResources().getString(R.string.module_mediaplayer_string_unlocked);
            BaseToast.showRoundRectToast(getContext(), string);
        }
    }

    @Override
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (mControlWrapper.isFullScreen()) {
            if (isVisible) {
                if (mLockButton.getVisibility() == GONE) {
                    mLockButton.setVisibility(VISIBLE);
                    if (anim != null) {
                        mLockButton.startAnimation(anim);
                    }
                }
            } else {
                mLockButton.setVisibility(GONE);
                if (anim != null) {
                    mLockButton.startAnimation(anim);
                }
            }
        }
    }

    /**
     * 播放模式
     * 普通模式，小窗口模式，正常模式三种其中一种
     * MODE_NORMAL              普通模式
     * MODE_FULL_SCREEN         全屏模式
     * MODE_TINY_WINDOW         小屏模式
     * @param playerState                   播放模式
     */
    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        switch (playerState) {
            case PlayerType.WindowType.NORMAL:
                setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mLockButton.setVisibility(GONE);
                break;
            case PlayerType.WindowType.FULL:
                if (isShowing()) {
                    mLockButton.setVisibility(VISIBLE);
                } else {
                    mLockButton.setVisibility(GONE);
                }
                break;
        }

        if (mActivity != null && hasCutout()) {
            int orientation = mActivity.getRequestedOrientation();
            int dp24 = PlayerUtils.dp2px(getContext(), 24);
            int cutoutHeight = getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                FrameLayout.LayoutParams lblp = (FrameLayout.LayoutParams) mLockButton.getLayoutParams();
                lblp.setMargins(dp24, 0, dp24, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24 + cutoutHeight, 0, dp24 + cutoutHeight, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24, 0, dp24, 0);
            }
        }

    }

    /**
     * 播放状态
     * -1               播放错误
     * 0                播放未开始
     * 1                播放准备中
     * 2                播放准备就绪
     * 3                正在播放
     * 4                暂停播放
     * 5                正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     * 6                暂停缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
     * 7                播放完成
     * 8                开始播放中止
     * @param playState                     播放状态，主要是指播放器的各种状态
     */
    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case PlayerType.StateType.STATE_IDLE:
                mLockButton.setSelected(false);
                mLoadingProgress.setVisibility(GONE);
                break;
            case PlayerType.StateType.STATE_PLAYING:
            case PlayerType.StateType.STATE_PAUSED:
            case PlayerType.StateType.STATE_PREPARED:
            case PlayerType.StateType.STATE_ERROR:
            case PlayerType.StateType.STATE_COMPLETED:
                mLoadingProgress.setVisibility(GONE);
                break;
            case PlayerType.StateType.STATE_PREPARING:
            case PlayerType.StateType.STATE_BUFFERING_PAUSED:
                mLoadingProgress.setVisibility(VISIBLE);
                break;
            case PlayerType.StateType.STATE_BUFFERING_PLAYING:
                mLoadingProgress.setVisibility(GONE);
                mLockButton.setVisibility(GONE);
                mLockButton.setSelected(false);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            String string = getContext().getResources().getString(R.string.module_mediaplayer_string_lock_tip);
            BaseToast.showRoundRectToast(getContext(), string);
            return true;
        }
        if (mControlWrapper.isFullScreen()) {
            return stopFullScreen();
        }
        Activity activity = PlayerUtils.scanForActivity(getContext());
        //如果不是全屏模式，则直接关闭页面activity
        if (PlayerUtils.isActivityLiving(activity)){
            activity.finish();
        }
        return super.onBackPressed();
    }

    /**
     * 刷新进度回调，子类可在此方法监听进度刷新，然后更新ui
     *
     * @param duration 视频总时长
     * @param position 视频当前时长
     */
    @Override
    protected void setProgress(int duration, int position) {
        super.setProgress(duration, position);
    }

    @Override
    public void destroy() {

    }

    public ImageView getThumb() {
        return thumb;
    }

    public void setTitle(String title) {
        if (titleView!=null){
            titleView.setTitle(title);
        }
    }

    public CustomBottomView getBottomView() {
        return vodControlView;
    }


    public TextView getTvLiveWaitMessage() {
        return tvLiveWaitMessage;
    }
}
