package lib.kalu.mediaplayer.core.render;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.core.kernel.KernelApi;

@Keep
public interface RenderApi {

    /**
     * 关联AbstractPlayer
     *
     * @param player player
     */
    void attachToPlayer(@NonNull KernelApi player);

    /**
     * 设置视频宽高
     *
     * @param videoWidth  宽
     * @param videoHeight 高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置视频旋转角度
     *
     * @param degree 角度值
     */
    void setVideoRotation(int degree);

    /**
     * 设置screen scale type
     *
     * @param scaleType 类型
     */
    void setScaleType(@PlayerType.ScaleType.Value int scaleType);

    /**
     * 获取真实的RenderView
     *
     * @return view
     */
    View getView();

    /**
     * 截图
     *
     * @return bitmap
     */
    Bitmap doScreenShot();

    /**
     * 释放资源
     */
    void release();
}