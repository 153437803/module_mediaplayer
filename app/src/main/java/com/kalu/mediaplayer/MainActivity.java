package com.kalu.mediaplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kalu.mediaplayer.newPlayer.activity.MultipleActivity;
import com.kalu.mediaplayer.ui.PortraitActivity;
import com.kalu.mediaplayer.newPlayer.surface.TestSurfaceActivity;
import com.kalu.mediaplayer.newPlayer.ad.AdActivity;
import com.kalu.mediaplayer.newPlayer.clarity.ClarityActivity;
import com.kalu.mediaplayer.newPlayer.danmu.DanmuActivity;
import com.kalu.mediaplayer.newPlayer.list.ContinuousVideoActivity;
import com.kalu.mediaplayer.newPlayer.list.TestListActivity;
import com.kalu.mediaplayer.newPlayer.pip.PipActivity;
import com.kalu.mediaplayer.newPlayer.pip.PipListActivity;
import com.kalu.mediaplayer.newPlayer.tiny.TestFullActivity;
import com.kalu.mediaplayer.newPlayer.tiny.TinyScreenActivity;

import lib.kalu.mediaplayer.ExoplayerActivity;
import lib.kalu.mediaplayer.kernel.video.factory.PlayerFactory;
import lib.kalu.mediaplayer.kernel.video.platfrom.exo.ExoMediaPlayer;
import lib.kalu.mediaplayer.kernel.video.platfrom.exo.ExoPlayerFactory;
import lib.kalu.mediaplayer.kernel.video.platfrom.ijk.IjkMediaPlayer;
import lib.kalu.mediaplayer.kernel.video.platfrom.ijk.IjkPlayerFactory;
import lib.kalu.mediaplayer.kernel.video.platfrom.media.AndroidMediaPlayer;
import lib.kalu.mediaplayer.kernel.video.platfrom.media.MediaPlayerFactory;
import lib.kalu.mediaplayer.ui.config.PlayerConfig;
import lib.kalu.mediaplayer.ui.config.PlayerConfigManager;
import lib.kalu.mediaplayer.ui.config.PlayerType;
import lib.kalu.mediaplayer.ui.tool.BaseToast;
import lib.kalu.mediaplayer.ui.tool.PlayerUtils;

import java.lang.reflect.Field;

/**
 * description:
 * created by kalu on 2021/11/23
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTitle;
    private TextView mTv11;
    private TextView mTv12;
    private TextView mTv13;
    private TextView mTv21;
    private TextView mTv22;
    private TextView mTv23;
    private TextView mTv31;
    private TextView mTv32;
    private TextView mTv33;
    private TextView mTv41;
    private TextView mTv42;
    private TextView mTv43;
    private TextView mTv61;
    private TextView mTv62;
    private TextView mTv63;
    private TextView mTv64;
    private TextView mTv65;
    private TextView mTv66;
    private TextView mTv71;
    private TextView mTv81;
    private TextView mTv101;
    private TextView mTv111;
    private TextView mTv131;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFindViewById();
        initListener();

        // 测试
//        Intent intent = new Intent(getApplicationContext(), ExoplayerActivity.class);
////        intent.putExtra(ExoplayerActivity.INTENT_URL, "rtsp://spinterface.hbtn.com:5566/filename.format?providerid=beijing66&assetid=hstv71778_1616050229&contentname=hstv71778_1616050229&duration=5418.00&bitrate=7992000");
//        intent.putExtra(ExoplayerActivity.INTENT_URL, "https://yunqivedio.alicdn.com/2017yq/v2/0x0/96d79d3f5400514a6883869399708e11/96d79d3f5400514a6883869399708e11.m3u8");
//        startActivity(intent);


        //检测当前是用的哪个播放器
        Object factory = PlayerUtils.getCurrentPlayerFactory();
        if (factory instanceof ExoPlayerFactory) {
            mTvTitle.setText("视频内核：" + " (ExoPlayer)");
            setTitle(getResources().getString(R.string.app_name) + " (ExoPlayer)");
        } else if (factory instanceof IjkPlayerFactory) {
            mTvTitle.setText("视频内核：" + " (IjkPlayer)");
        } else if (factory instanceof MediaPlayerFactory) {
            mTvTitle.setText("视频内核：" + " (MediaPlayer)");
        } else {
            mTvTitle.setText("视频内核：" + " (unknown)");
        }

        // 横屏
        findViewById(R.id.tv_3_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = findViewById(R.id.url);
                Editable text = editText.getText();

                Intent intent = new Intent(getApplicationContext(), ExoplayerActivity.class);
                intent.putExtra(ExoplayerActivity.INTENT_URL, text.toString());
//                intent.putExtra(ExoplayerActivity.INTENT_URL, "http://10.128.202.122:8001/ocrm/api/video/getVideoDetail/a1a11426813347ceb818dee0edbab3d5");
//                intent.putExtra(ExoplayerActivity.INTENT_URL, "https://yunqivedio.alicdn.com/2017yq/v2/0x0/96d79d3f5400514a6883869399708e11/96d79d3f5400514a6883869399708e11.m3u8");
                startActivity(intent);
            }
        });
    }

    private void initFindViewById() {
        mTvTitle = findViewById(R.id.tv_title);
        mTv11 = findViewById(R.id.tv_1_1);
        mTv12 = findViewById(R.id.tv_1_2);
        mTv13 = findViewById(R.id.tv_1_3);
        mTv21 = findViewById(R.id.tv_2_1);
        mTv22 = findViewById(R.id.tv_2_2);
        mTv23 = findViewById(R.id.tv_2_3);
        mTv31 = findViewById(R.id.tv_3_1);
        mTv32 = findViewById(R.id.tv_3_2);
        mTv33 = findViewById(R.id.tv_3_3);
        mTv41 = findViewById(R.id.tv_4_1);
        mTv42 = findViewById(R.id.tv_4_2);
        mTv43 = findViewById(R.id.tv_4_3);
        mTv61 = findViewById(R.id.tv_6_1);
        mTv62 = findViewById(R.id.tv_6_2);
        mTv63 = findViewById(R.id.tv_6_3);
        mTv64 = findViewById(R.id.tv_6_4);
        mTv65 = findViewById(R.id.tv_6_5);
        mTv66 = findViewById(R.id.tv_6_6);
        mTv71 = findViewById(R.id.tv_7_1);
        mTv81 = findViewById(R.id.tv_8_1);
        mTv101 = findViewById(R.id.tv_10_1);
        mTv111 = findViewById(R.id.tv_11_1);
        mTv131 = findViewById(R.id.tv_13_1);
    }

    private void initListener() {
        mTv11.setOnClickListener(this);
        mTv12.setOnClickListener(this);
        mTv13.setOnClickListener(this);
        mTv21.setOnClickListener(this);
        mTv22.setOnClickListener(this);
        mTv23.setOnClickListener(this);
        mTv31.setOnClickListener(this);
        mTv32.setOnClickListener(this);
        mTv33.setOnClickListener(this);
        mTv41.setOnClickListener(this);
        mTv42.setOnClickListener(this);
        mTv43.setOnClickListener(this);
        mTv61.setOnClickListener(this);
        mTv62.setOnClickListener(this);
        mTv63.setOnClickListener(this);
        mTv64.setOnClickListener(this);
        mTv65.setOnClickListener(this);
        mTv66.setOnClickListener(this);
        mTv71.setOnClickListener(this);
        mTv81.setOnClickListener(this);
        mTv101.setOnClickListener(this);
        mTv111.setOnClickListener(this);
        mTv131.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv11) {
            //切换ijk
            setChangeVideoType(PlayerType.PlatformType.IJK);
        } else if (v == mTv12) {
            //切换exo
            setChangeVideoType(PlayerType.PlatformType.EXO);
        } else if (v == mTv13) {
            //切换原生
            setChangeVideoType(PlayerType.PlatformType.NATIVE);
        } else if (v == mTv21) {
            BaseToast.showRoundRectToast(getApplicationContext(), "待完善");
        } else if (v == mTv22) {
            BaseToast.showRoundRectToast(getApplicationContext(), "待完善");
        } else if (v == mTv23) {
            BaseToast.showRoundRectToast(getApplicationContext(), "待完善");
        } else if (v == mTv31) {
            startActivity(new Intent(this, PortraitActivity.class));
        } else if (v == mTv32) {
            startActivity(new Intent(this, TestFullActivity.class));
        } else if (v == mTv33) {
            startActivity(new Intent(this, MultipleActivity.class));
        } else if (v == mTv41) {
            startActivity(new Intent(this, PipActivity.class));
        } else if (v == mTv42) {
            startActivity(new Intent(this, PipListActivity.class));
        } else if (v == mTv43) {
            startActivity(new Intent(this, TinyScreenActivity.class));
        } else if (v == mTv61) {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        } else if (v == mTv62) {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        } else if (v == mTv63) {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        } else if (v == mTv64) {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
        } else if (v == mTv65) {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("type", 4);
            startActivity(intent);
        } else if (v == mTv66) {
            Intent intent = new Intent(this, TestListActivity.class);
            intent.putExtra("type", 5);
            startActivity(intent);
        } else if (v == mTv71) {
            startActivity(new Intent(this, DanmuActivity.class));
        } else if (v == mTv81) {
            startActivity(new Intent(this, AdActivity.class));
        } else if (v == mTv101) {
            startActivity(new Intent(this, ContinuousVideoActivity.class));
        } else if (v == mTv111) {
            startActivity(new Intent(this, ClarityActivity.class));
        } else if (v == mTv131) {
            startActivity(new Intent(this, TestSurfaceActivity.class));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setChangeVideoType(@PlayerType.PlatformType int type) {
        //切换播放核心，不推荐这么做，我这么写只是为了方便测试
        PlayerConfig config = PlayerConfigManager.getInstance().getConfig();
        try {
            Field mPlayerFactoryField = config.getClass().getDeclaredField("mPlayerFactory");
            mPlayerFactoryField.setAccessible(true);
            PlayerFactory playerFactory = null;
            switch (type) {
                case PlayerType.PlatformType.IJK:
                    playerFactory = IjkPlayerFactory.create();
                    IjkMediaPlayer ijkVideoPlayer = (IjkMediaPlayer) playerFactory.createPlayer(getApplicationContext());
                    mTvTitle.setText("视频内核：" + " (IjkPlayer)");
                    break;
                case PlayerType.PlatformType.EXO:
                    playerFactory = ExoPlayerFactory.create();
                    ExoMediaPlayer exoMediaPlayer = (ExoMediaPlayer) playerFactory.createPlayer(getApplicationContext());
                    mTvTitle.setText("视频内核：" + " (ExoPlayer)");
                    break;
                case PlayerType.PlatformType.NATIVE:
                    playerFactory = MediaPlayerFactory.create();
                    AndroidMediaPlayer androidMediaPlayer = (AndroidMediaPlayer) playerFactory.createPlayer(getApplicationContext());
                    mTvTitle.setText("视频内核：" + " (MediaPlayer)");
                    break;
            }
            mPlayerFactoryField.set(config, playerFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
