package lib.kalu.mediaplayer.core.kernel.android;

import android.content.Context;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import lib.kalu.mediaplayer.core.kernel.KernelEvent;
import lib.kalu.mediaplayer.core.kernel.KernelFactory;

@Keep
public class AndroidFactory extends KernelFactory<AndroidMediaPlayer> {

    private AndroidFactory() {
    }

//    private static class Holder {
//        static final AndroidMediaPlayer mP = new AndroidMediaPlayer();
//    }

    public static AndroidFactory build() {
        return new AndroidFactory();
    }

    @Override
    public AndroidMediaPlayer createKernel(@NonNull Context context, @NonNull KernelEvent event) {
        return new AndroidMediaPlayer(event);
//        return Holder.mP;
    }
}
