package com.kalu.mediaplayer.newPlayer.tiny;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.kalu.mediaplayer.BaseActivity;
import com.kalu.mediaplayer.ConstantVideo;

import com.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.videoui.config.ConstantKeys;
import lib.kalu.mediaplayer.videoui.player.VideoLayout;
import lib.kalu.mediaplayer.videoui.ui.view.DefaultController;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;


/**
 * @author yc
 */
public class TestFullActivity extends BaseActivity implements View.OnClickListener {

    private VideoLayout mVideoPlayerLayout;
    private Button mBtnTiny1;
    private Button mBtnTiny2;

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoPlayerLayout != null) {
            mVideoPlayerLayout.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoPlayerLayout != null) {
            mVideoPlayerLayout.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlayerLayout != null) {
            mVideoPlayerLayout.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoPlayerLayout == null || !mVideoPlayerLayout.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_full_video1;
    }

    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this, true);
        adaptCutoutAboveAndroidP();
        mVideoPlayerLayout = findViewById(R.id.video_player);
        mBtnTiny1 = (Button) findViewById(R.id.btn_tiny_1);
        mBtnTiny2 = (Button) findViewById(R.id.btn_tiny_2);

        DefaultController controller = new DefaultController(this);
        //设置视频背景图
        Glide.with(this).load(R.drawable.image_default).into(controller.getThumb());
        //设置控制器
        mVideoPlayerLayout.setController(controller);
        mVideoPlayerLayout.setUrl(ConstantVideo.VideoPlayerList[0]);
        mVideoPlayerLayout.setScreenScaleType(ConstantKeys.PlayerScreenScaleType.SCREEN_SCALE_16_9);
        mVideoPlayerLayout.start();
    }

    @Override
    public void initListener() {
        mBtnTiny1.setOnClickListener(this);
        mBtnTiny2.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tiny_1:
                mVideoPlayerLayout.startFullScreen();
                break;
            case R.id.btn_tiny_2:
                mVideoPlayerLayout.startTinyScreen();
                break;
            default:
                break;
        }
    }

    private void adaptCutoutAboveAndroidP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }


}
