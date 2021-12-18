package com.kalu.mediaplayer.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kalu.mediaplayer.BuriedPointEventImpl;
import com.kalu.mediaplayer.ConstantVideo;
import com.kalu.mediaplayer.R;

import lib.kalu.mediaplayer.controller.component.ComponentError;
import lib.kalu.mediaplayer.controller.standard.ControllerStandard;
import lib.kalu.mediaplayer.kernel.video.factory.PlayerFactory;
import lib.kalu.mediaplayer.config.PlayerConfig;
import lib.kalu.mediaplayer.config.PlayerType;
import lib.kalu.mediaplayer.listener.OnVideoStateListener;
import lib.kalu.mediaplayer.widget.player.VideoBuilder;
import lib.kalu.mediaplayer.widget.player.VideoLayout;
import lib.kalu.mediaplayer.config.PlayerConfigManager;
import lib.kalu.mediaplayer.util.PlayerFactoryUtils;

public class PortraitActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoLayout mVideoPlayer;
    private Button mBtnScaleNormal;
    private Button mBtnScale169;
    private Button mBtnScale43;
    private Button mBtnScaleFull;
    private Button mBtnScaleOriginal;
    private Button mBtnScaleCrop;
    private Button mBtnCrop;
    private Button mBtnGif;
//    private BasisVideoController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initFindViewById();
        initVideoPlayer();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoPlayer != null) {
            //从后台切换到前台，当视频暂停时或者缓冲暂停时，调用该方法重新开启视频播放
            mVideoPlayer.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoPlayer != null) {
            //从前台切到后台，当视频正在播放或者正在缓冲时，调用该方法暂停视频
            mVideoPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlayer != null) {
            //销毁页面，释放，内部的播放器被释放掉，同时如果在全屏、小窗口模式下都会退出
            mVideoPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        //处理返回键逻辑；如果是全屏，则退出全屏；如果是小窗口，则退出小窗口
        if (mVideoPlayer == null || !mVideoPlayer.onBackPressed()) {

            long currentPosition = mVideoPlayer.getCurrentPosition();
            long duration = mVideoPlayer.getDuration();
            Toast.makeText(getApplicationContext(), currentPosition + "-" + duration, Toast.LENGTH_SHORT).show();

            super.onBackPressed();
        }
    }

    private void initFindViewById() {
        mVideoPlayer = findViewById(R.id.video_player);
        mBtnScaleNormal = findViewById(R.id.btn_scale_normal);
        mBtnScale169 = findViewById(R.id.btn_scale_169);
        mBtnScale43 = findViewById(R.id.btn_scale_43);
        mBtnScaleFull = findViewById(R.id.btn_scale_full);
        mBtnScaleOriginal = findViewById(R.id.btn_scale_original);
        mBtnScaleCrop = findViewById(R.id.btn_scale_crop);
        mBtnCrop = findViewById(R.id.btn_crop);
        mBtnGif = findViewById(R.id.btn_gif);
    }

    private void initVideoPlayer() {
//        String url = getIntent().getStringExtra(IntentKeys.URL);
        String url = "https://yunqivedio.alicdn.com/2017yq/v2/0x0/96d79d3f5400514a6883869399708e11/96d79d3f5400514a6883869399708e11.m3u8";
//        String url = "https://vod.iartschool.com/793497282635890688_low.m3u8?sign=52a09ee9f0f03b0270339dab57712f9a&t=60990a84";
        if (url == null || url.length() == 0) {
            url = ConstantVideo.VideoPlayerList[0];
        }
        //创建基础视频播放器，一般播放器的功能


        ControllerStandard basisVideoController = new ControllerStandard(this);
        basisVideoController.setEnableOrientation(false);
        //设置控制器
        mVideoPlayer.setController(basisVideoController);


        //设置视频播放链接地址
        //开始播放
        mVideoPlayer.startFullScreen();
        mVideoPlayer.start(url);
//        mVideoPlayer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mVideoPlayer.start();
//            }
//        },300);
    }

    private void initListener() {
        mBtnScaleNormal.setOnClickListener(this);
        mBtnScale169.setOnClickListener(this);
        mBtnScale43.setOnClickListener(this);
        mBtnScaleFull.setOnClickListener(this);
        mBtnScaleOriginal.setOnClickListener(this);
        mBtnScaleCrop.setOnClickListener(this);
        mBtnCrop.setOnClickListener(this);
        mBtnGif.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mBtnScale169) {
            mVideoPlayer.setScreenScaleType(PlayerType.ScaleType.SCREEN_SCALE_16_9);
        } else if (v == mBtnScaleNormal) {
            mVideoPlayer.setScreenScaleType(PlayerType.ScaleType.SCREEN_SCALE_DEFAULT);
        } else if (v == mBtnScale43) {
            mVideoPlayer.setScreenScaleType(PlayerType.ScaleType.SCREEN_SCALE_4_3);
        } else if (v == mBtnScaleFull) {
            mVideoPlayer.setScreenScaleType(PlayerType.ScaleType.SCREEN_SCALE_MATCH_PARENT);
        } else if (v == mBtnScaleOriginal) {
            mVideoPlayer.setScreenScaleType(PlayerType.ScaleType.SCREEN_SCALE_ORIGINAL);
        } else if (v == mBtnScaleCrop) {
            mVideoPlayer.setScreenScaleType(PlayerType.ScaleType.SCREEN_SCALE_CENTER_CROP);
        } else if (v == mBtnCrop) {

        } else if (v == mBtnGif) {

        }
    }

    private void test() {
        //VideoPlayer相关
        VideoBuilder.Builder builder = VideoBuilder.newBuilder();
        VideoBuilder videoPlayerBuilder = new VideoBuilder(builder);
        //设置视频播放器的背景色
        builder.setPlayerBackgroundColor(Color.BLACK);
        //设置小屏的宽高
        int[] mTinyScreenSize = {0, 0};
        builder.setTinyScreenSize(mTinyScreenSize);
        mVideoPlayer.setVideoBuilder(videoPlayerBuilder);
        //截图
        Bitmap bitmap = mVideoPlayer.doScreenShot();
        //移除所有播放状态监听
        mVideoPlayer.clearOnStateChangeListeners();
        //获取当前缓冲百分比
        int bufferedPercentage = mVideoPlayer.getBufferedPercentage();
        //获取当前播放器的状态
        int currentPlayerState = mVideoPlayer.getCurrentWindowState();
        //获取当前的播放状态
        int currentPlayState = mVideoPlayer.getCurrentPlayState();
        //获取当前播放的位置
        long currentPosition = mVideoPlayer.getCurrentPosition();
        //获取视频总时长
        long duration = mVideoPlayer.getDuration();
        //获取倍速速度
        float speed = mVideoPlayer.getSpeed();
        //获取缓冲速度
        long tcpSpeed = mVideoPlayer.getTcpSpeed();
        //获取视频宽高
        int[] videoSize = mVideoPlayer.getVideoSize();
        //是否处于静音状态
        boolean mute = mVideoPlayer.isMute();
        //判断是否处于全屏状态
        boolean fullScreen = mVideoPlayer.isFullScreen();
        //是否是小窗口模式
        boolean tinyScreen = mVideoPlayer.isTinyScreen();

        //是否处于播放状态
        boolean playing = mVideoPlayer.isPlaying();
        //暂停播放
        mVideoPlayer.pause();
        //视频缓冲完毕，准备开始播放时回调
        mVideoPlayer.onPrepared();
        //重新播放
        mVideoPlayer.restart(true);
        //继续播放
        mVideoPlayer.resume();
        //调整播放进度
        mVideoPlayer.seekTo(100);
        //循环播放， 默认不循环播放
        mVideoPlayer.setLooping(true);
        //设置播放速度
        mVideoPlayer.setSpeed(1.1f);
        //设置音量 0.0f-1.0f 之间
        mVideoPlayer.setVolume(1, 1);
        //开始播放
        mVideoPlayer.start("");


        //进入全屏
        mVideoPlayer.startFullScreen();
        //退出全屏
        mVideoPlayer.stopFullScreen();
        //开启小屏
        mVideoPlayer.startTinyScreen();
        //退出小屏
        mVideoPlayer.stopTinyScreen();

        mVideoPlayer.setOnStateChangeListener(new OnVideoStateListener() {
            /**
             * 播放模式
             * 普通模式，小窗口模式，正常模式三种其中一种
             * MODE_NORMAL              普通模式
             * MODE_FULL_SCREEN         全屏模式
             * MODE_TINY_WINDOW         小屏模式
             * @param playerState                       播放模式
             */
            @Override
            public void onWindowStateChanged(int playerState) {
                switch (playerState) {
                    case PlayerType.WindowType.NORMAL:
                        //普通模式
                        break;
                    case PlayerType.WindowType.FULL:
                        //全屏模式
                        break;
                    case PlayerType.WindowType.TINY:
                        //小屏模式
                        break;
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
             * @param playState                         播放状态，主要是指播放器的各种状态
             */
            @Override
            public void onPlayStateChanged(int playState) {
                switch (playState) {
                    case PlayerType.StateType.STATE_IDLE:
                        //播放未开始，初始化
                        break;
                    case PlayerType.StateType.STATE_START_ABORT:
                        //开始播放中止
                        break;
                    case PlayerType.StateType.STATE_PREPARING:
                        //播放准备中
                        break;
                    case PlayerType.StateType.STATE_PREPARED:
                        //播放准备就绪
                        break;
                    case PlayerType.StateType.STATE_ERROR:
                        //播放错误
                        break;
                    case PlayerType.StateType.STATE_BUFFERING_PLAYING:
                        //正在缓冲
                        break;
                    case PlayerType.StateType.STATE_PLAYING:
                        //正在播放
                        break;
                    case PlayerType.StateType.STATE_PAUSED:
                        //暂停播放
                        break;
                    case PlayerType.StateType.STATE_BUFFERING_PAUSED:
                        //暂停缓冲
                        break;
                    case PlayerType.StateType.STATE_COMPLETED:
                        //播放完成
                        break;
                }
            }
        });

        //设置视频背景图
        ControllerStandard controller = (ControllerStandard) mVideoPlayer.getVideoController();
        //设置视频标题
        controller.setTitle("视频标题");
        //添加自定义视图。每添加一个视图，都是方式层级树的最上层
        ComponentError customErrorView = new ComponentError(this);
        controller.addComponent(customErrorView);
        //移除控制组件
        controller.removeComponent(customErrorView);
        //移除所有的组件
        controller.removeComponentAll(false);
        //隐藏播放视图
        controller.hide();
        //显示播放视图
        controller.show();
        //是否开启根据屏幕方向进入/退出全屏
        controller.setEnableOrientation(true);
        //显示移动网络播放提示
        controller.showNetWarning();
        //刘海的高度
        int cutoutHeight = controller.getCutoutHeight();
        //是否有刘海屏
        boolean b = controller.hasCutout();
        //设置是否适配刘海屏
        controller.setAdaptCutout(true);
        //停止刷新进度
        controller.stopProgress();
        //开始刷新进度，注意：需在STATE_PLAYING时调用才会开始刷新进度
        controller.startProgress();
        //判断是否锁屏
        boolean locked = controller.isLocked();
        //设置是否锁屏
        controller.setLocked(true);
        //取消计时
        controller.stopFadeOut();
        //开始计时
        controller.startFadeOut();
        //设置播放视图自动隐藏超时
        controller.setDismissTimeout(8);
        //销毁
        controller.destroy();


        //播放器配置，注意：此为全局配置，按需开启
        PlayerFactory player = PlayerFactoryUtils.getPlayer(PlayerType.PlatformType.IJK);
        PlayerConfigManager.getInstance().setConfig(PlayerConfig.newBuilder()
                //设置视频全局埋点事件
                .setBuriedPointEvent(new BuriedPointEventImpl())
                //调试的时候请打开日志，方便排错
                .setLogEnabled(true)
                //设置ijk
                .setPlayerFactory(player)
                //在移动环境下调用start()后是否继续播放，默认不继续播放
                .setPlayOnMobileNetwork(false)
                //是否适配刘海屏，默认适配
                .setAdaptCutout(true)
                //监听设备方向来切换全屏/半屏， 默认不开启
                .setEnableOrientation(false)
                //设置自定义渲染view，自定义RenderView
                //.setRenderViewFactory(null)
                //创建
                .build());
    }
}
