package com.kalu.mediaplayer.demo;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.kalu.mediaplayer.ConstantVideo;

import com.kalu.mediaplayer.R;

import lib.kalu.mediaplayer.kernel.video.platfrom.exo.ExoPlayerFactory;

public class ExoActivity extends AppCompatActivity {

    private PlayerView mVideoView;
    private SimpleExoPlayer player;

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        mVideoView = findViewById(R.id.video_view);

        ExoPlayer player = ExoPlayerFactory.create().createPlayer(getApplicationContext()).getPlayer();
        player.setPlayWhenReady(true);
        mVideoView.setPlayer(player);
        Uri uri = Uri.parse(ConstantVideo.VideoPlayerList[0]);
        DefaultHttpDataSource.Factory factory = new DefaultHttpDataSource.Factory();
        factory.setUserAgent("user-agent");
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(uri);
        // 播放
        player.prepare(mediaSource);
    }
}
