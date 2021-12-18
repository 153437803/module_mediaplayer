package lib.kalu.mediaplayer.core.kernel.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Keep;

import lib.kalu.mediaplayer.util.MediaLogUtil;
import lib.kalu.mediaplayer.core.kernel.music.config.MusicPlayAction;
import lib.kalu.mediaplayer.core.kernel.music.service.PlayService;
import lib.kalu.mediaplayer.core.kernel.music.tool.BaseAppHelper;

@Keep
public class NotificationStatusBarReceiver extends BroadcastReceiver {

    public static final String ACTION_STATUS_BAR = "YC_ACTION_STATUS_BAR";
    public static final String EXTRA = "extra";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra, MusicPlayAction.TYPE_NEXT)) {
            PlayService.startCommand(context, MusicPlayAction.TYPE_NEXT);
            MediaLogUtil.log("NotifiyStatusBarReceiver"+"下一首");
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_START_PAUSE)) {
            if(BaseAppHelper.get().getPlayService()!=null){
                boolean playing = BaseAppHelper.get().getPlayService().isPlaying();
                if(playing){
                    MediaLogUtil.log("NotifiyStatusBarReceiver"+"暂停");
                }else {
                    MediaLogUtil.log("NotifiyStatusBarReceiver"+"播放");
                }
                PlayService.startCommand(context, MusicPlayAction.TYPE_START_PAUSE);
            }

        }else if(TextUtils.equals(extra, MusicPlayAction.TYPE_PRE)){
            PlayService.startCommand(context, MusicPlayAction.TYPE_PRE);
            MediaLogUtil.log("NotifiyStatusBarReceiver"+"上一首");
        }
    }
}
