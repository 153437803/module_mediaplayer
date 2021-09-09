package lib.kalu.mediaplayer;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import lib.kalu.mediaplayer.videoui.config.ConstantKeys;
import lib.kalu.mediaplayer.videoui.player.OnVideoStateListener;
import lib.kalu.mediaplayer.videoui.player.VideoLayout;
import lib.kalu.mediaplayer.videoui.ui.view.BasisVideoController;

/**
 * @description: 横屏全屏视频播放器
 * @date: 2021-05-25 10:37
 */
public final class ExoplayerActivity extends AppCompatActivity {

    @Keep
    public static final int RESULT_CODE = 31001;
    @Keep
    public static final String INTENT_DADA = "intent_dada";
    @Keep
    public static final String INTENT_URL = "intent_url";
    @Keep
    public static final String INTENT_TIME_BROWSING = "intent_time_browsing";
    @Keep
    public static final String INTENT_TIME_LENGTH = "intent_time_length";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.module_mediaplayer_activity_exoplayer);

        String url = getIntent().getStringExtra(INTENT_URL);
        if (null == url || url.length() == 0 || !url.startsWith("http")) {
            onBackPressed();
            return;
        }

        VideoLayout videoLayout = findViewById(R.id.module_mediaplayer_video);
        // 基础视频播放器
        BasisVideoController basisVideoController = new BasisVideoController(this);
        basisVideoController.setEnableOrientation(false);
        // 设置视频背景图
        ColorDrawable colorDrawable = new ColorDrawable(Color.BLACK);
        basisVideoController.getThumb().setImageDrawable(colorDrawable);
        // 控制器
        videoLayout.setController(basisVideoController);
        // 设置视频播放链接地址
        videoLayout.setUrl(url);
        videoLayout.showNetWarning();
        // 全屏
        videoLayout.startFullScreen();
        // 开始播放
        videoLayout.start();

        // 监听
        videoLayout.setOnStateChangeListener(new OnVideoStateListener() {
            /**
             * 播放模式
             * 普通模式，小窗口模式，正常模式三种其中一种
             * MODE_NORMAL              普通模式
             * MODE_FULL_SCREEN         全屏模式
             * MODE_TINY_WINDOW         小屏模式
             * @param playerState                       播放模式
             */
            @Override
            public void onPlayerStateChanged(int playerState) {
                switch (playerState) {
                    case ConstantKeys.PlayMode.MODE_NORMAL:
                        onBackPressed();
                        //普通模式
                        break;
                    case ConstantKeys.PlayMode.MODE_FULL_SCREEN:
                        //全屏模式
                        break;
                    case ConstantKeys.PlayMode.MODE_TINY_WINDOW:
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
                    case ConstantKeys.CurrentState.STATE_IDLE:
                        //播放未开始，初始化
                        break;
                    case ConstantKeys.CurrentState.STATE_START_ABORT:
                        //开始播放中止
                        break;
                    case ConstantKeys.CurrentState.STATE_PREPARING:
                        //播放准备中
                        break;
                    case ConstantKeys.CurrentState.STATE_PREPARED:
                        //播放准备就绪
                        break;
                    case ConstantKeys.CurrentState.STATE_ERROR:
                        //播放错误
                        break;
                    case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
                        //正在缓冲
                        break;
                    case ConstantKeys.CurrentState.STATE_PLAYING:
                        //正在播放
                        break;
                    case ConstantKeys.CurrentState.STATE_PAUSED:
                        //暂停播放
                        break;
                    case ConstantKeys.CurrentState.STATE_BUFFERING_PAUSED:
                        //暂停缓冲
                        break;
                    case ConstantKeys.CurrentState.STATE_COMPLETED:
                        //播放完成
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        VideoLayout videoLayout = findViewById(R.id.module_mediaplayer_video);
        if (videoLayout == null || !videoLayout.onBackPressed()) {
            finish();
        }
    }

    @Override
    public void finish() {

        VideoLayout videoLayout = findViewById(R.id.module_mediaplayer_video);
        if (videoLayout != null) {

            // s
            long currentPosition = videoLayout.getCurrentPosition() / 1000;
            // s
            long duration = videoLayout.getDuration() / 1000;

            if (currentPosition < 0) {
                currentPosition = 0;
            }
            if (duration < 0) {
                duration = 0;
            }

            String extra = getIntent().getStringExtra(INTENT_DADA);
            Intent intent = new Intent();
            intent.putExtra(INTENT_DADA, extra);
            intent.putExtra(INTENT_TIME_LENGTH, duration);
            intent.putExtra(INTENT_TIME_BROWSING, currentPosition);
            setResult(RESULT_CODE, intent);
        }
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VideoLayout videoLayout = findViewById(R.id.module_mediaplayer_video);
        videoLayout.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoLayout videoLayout = findViewById(R.id.module_mediaplayer_video);
        videoLayout.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoLayout videoLayout = findViewById(R.id.module_mediaplayer_video);
        videoLayout.release();

        try {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            System.exit(0);
        }
    }
}