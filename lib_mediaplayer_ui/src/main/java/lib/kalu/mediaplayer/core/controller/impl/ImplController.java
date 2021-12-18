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
package lib.kalu.mediaplayer.core.controller.impl;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 视频控制器接口
 *     revise: 定义一些设置视图属性接口
 * </pre>
 */
public interface ImplController {

    /**
     * 初始化
     */
    void init();

    /**
     * 控制器意外销毁，比如手动退出，意外崩溃等等
     */
    void destroy();

    /**
     * 设置控制器布局文件，子类必须实现
     */
    int initLayout();

    /*******************/

    /**
     * 显示播放控制菜单
     */
    void hide();

    /**
     * 隐藏控制视图
     */
    void show();

    /*******************/

    /**
     * 开始控制视图自动隐藏倒计时
     */
    void startFadeOut();

    /**
     * 取消控制视图自动隐藏倒计时
     */
    void stopFadeOut();

    /**
     * 控制视图是否处于显示状态
     */
    boolean isShowing();

    /**
     * 设置锁定状态
     *
     * @param locked 是否锁定
     */
    void setLocked(boolean locked);

    /**
     * 是否处于锁定状态
     */
    boolean isLocked();

    /**
     * 开始刷新进度
     */
    void startProgress();

    /**
     * 停止刷新进度
     */
    void stopProgress();

    /**
     * 是否需要适配刘海
     */
    boolean hasCutout();

    /**
     * 获取刘海的高度
     */
    int getCutoutHeight();


    /************************/

    @Nullable
    default ImageView findPrepare() {
        return null;
    }

    default void setPrepareBackground(@ColorInt int color) {
        ImageView view = findPrepare();
        if (null != view) {
            view.setImageDrawable(null);
            view.setBackgroundColor(color);
        }
    }

    default void setPrepareImageResource(@DrawableRes int resId) {
        ImageView view = findPrepare();
        if (null != view) {
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setImageResource(resId);
        }
    }

    default void setPrepareImageDrawable(@NonNull Drawable drawable) {
        ImageView view = findPrepare();
        if (null != view) {
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setImageDrawable(drawable);
        }
    }

    default void setPrepareImageUrl(@NonNull String url) {

        if (null == url || url.length() <= 0)
            return;

        ImageView imageView = findPrepare();
        if (null == imageView)
            return;

        try {
            imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setImageURI(Uri.parse(url));
        }catch (Exception e){
        }
    }

    /************************/
}
