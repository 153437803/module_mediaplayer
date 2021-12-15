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
package lib.kalu.mediaplayer.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import lib.kalu.mediaplayer.BuildConfig;

/**
 * description: LogUtil
 * created by kalu on 2021/9/29
 */
public final class LogUtil {

    private static boolean isLog = BuildConfig.DEBUG;

    public static void setIsLog(boolean isLog) {
        LogUtil.isLog = isLog;
    }

    public static boolean isIsLog() {
        return isLog;
    }

    public static void log(@NonNull String message) {
        log(message, null);
    }

    public static void log(@NonNull String message, @Nullable Throwable throwable) {

        if (!isLog)
            return;

        if (null == message || message.length() == 0)
            return;

        Log.e("module_mediaplayer", message, throwable);
    }
}
