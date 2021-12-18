package com.kalu.mediaplayer.newPlayer.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.kalu.mediaplayer.R;

import com.kalu.mediaplayer.newPlayer.activity.DetailActivity;
import com.kalu.mediaplayer.newPlayer.activity.IntentKeys;

import lib.kalu.mediaplayer.config.PlayerType;
import lib.kalu.mediaplayer.config.VideoInfoBean;
import lib.kalu.mediaplayer.config.PlayerConfigManager;
import lib.kalu.mediaplayer.util.PlayerUtils;

/**
 * 无缝播放
 */
public class SeamlessPlayFragment extends RecyclerViewAutoPlayFragment {

    private boolean mSkipToDetail;

    @SuppressLint("WrongConstant")
    @Override
    protected void initView(View view) {
        super.initView(view);

        //提前添加到VideoViewManager，供详情使用
        PlayerConfigManager.instance().add(mVideoView, "seamless");

        mAdapter.setOnItemClickListener(position -> {
            mSkipToDetail = true;
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            Bundle bundle = new Bundle();
            VideoInfoBean videoBean = mVideos.get(position);
            if (mCurPos == position) {
                //需要无缝播放
                bundle.putBoolean(IntentKeys.SEAMLESS_PLAY, true);
                bundle.putString(IntentKeys.TITLE, videoBean.getTitle());
            } else {
                //无需无缝播放，把相应数据传到详情页
                mVideoView.release();
                //需要把控制器还原
                mController.setPlayState(PlayerType.StateType.STATE_IDLE);
                bundle.putBoolean(IntentKeys.SEAMLESS_PLAY, false);
                bundle.putString(IntentKeys.URL, videoBean.getVideoUrl());
                bundle.putString(IntentKeys.TITLE, videoBean.getTitle());
                mCurPos = position;
            }
            intent.putExtras(bundle);
            View sharedView = mLinearLayoutManager.findViewByPosition(position).findViewById(R.id.player_container);
            //使用共享元素动画
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), sharedView, DetailActivity.VIEW_NAME_PLAYER_CONTAINER);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        });
    }

    @Override
    protected void startPlay(int position) {
        mVideoView.setController(mController);
        super.startPlay(position);
    }

    @Override
    protected void pause() {
        if (!mSkipToDetail) {
            super.pause();
        }
    }

    @Override
    protected void resume() {
        if (mSkipToDetail) {
            mSkipToDetail = false;
        } else {
            super.resume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || !addTransitionListener()) {
            restoreVideoView();
        }
    }

    @RequiresApi(21)
    private boolean addTransitionListener() {
        final Transition transition = getActivity().getWindow().getSharedElementExitTransition();
        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    restoreVideoView();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
            return true;
        }
        return false;
    }

    @SuppressLint("WrongConstant")
    private void restoreVideoView() {
        //还原播放器
        View itemView = mLinearLayoutManager.findViewByPosition(mCurPos);
        VideoRecyclerViewAdapter.VideoHolder viewHolder = (VideoRecyclerViewAdapter.VideoHolder) itemView.getTag();
        mVideoView = PlayerConfigManager.instance().get("seamless");
        PlayerUtils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);

        mController.add(viewHolder.mPrepareView, true);
        mController.setPlayState(mVideoView.getCurrentPlayState());
        mController.setWindowState(mVideoView.getCurrentWindowState());

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVideoView.setController(mController);
            }
        }, 100);
    }
}
