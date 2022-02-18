package lib.kalu.mediaplayer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import lib.kalu.mediaplayer.R;

@Keep
@SuppressLint("AppCompatCustomView")
public class MediaProgressBar extends TextView {

    public MediaProgressBar(Context context) {
        super(context);
        init(null);
    }

    public MediaProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MediaProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MediaProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private int mCount = 0;
    private float mRadius = 0f;
    private float mRate = 0f;

    @ColorInt
    private int mColorCanvas = Color.TRANSPARENT;
    @ColorInt
    private int mColorBackground = Color.TRANSPARENT;
    @ColorInt
    private int mColorRound = Color.GRAY;

    private final void init(@Nullable AttributeSet attrs) {
//        setEnabled(true);

        TypedArray typedArray = null;

        try {
            typedArray = getContext().getApplicationContext().obtainStyledAttributes(attrs, R.styleable.MediaProgressBar);
            if (mCount == 0) {
                mCount = typedArray.getInt(R.styleable.MediaProgressBar_mpb_count, 8);
            }
            if (mRate == 0f) {
                mRate = typedArray.getFloat(R.styleable.MediaProgressBar_mpb_rate, 0.9f);
            }
            if (mRadius == 0f) {
                mRadius = typedArray.getDimension(R.styleable.MediaProgressBar_mpb_radius, 0f);
            }

            mColorCanvas = typedArray.getColor(R.styleable.MediaProgressBar_mpb_color_canvas, Color.TRANSPARENT);
            mColorBackground = typedArray.getColor(R.styleable.MediaProgressBar_mpb_color_background, Color.TRANSPARENT);
            mColorRound = typedArray.getColor(R.styleable.MediaProgressBar_mpb_color_round, Color.GRAY);
        } catch (Exception e) {
        }

        if (null != typedArray) {
            typedArray.recycle();
        }
        setTag(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (View.VISIBLE != getVisibility())
            return;

        // 循环次数
        int num;
        try {
            num = (int) getTag();
            if (num + 1 >= mCount) {
                num = 0;
            }
        } catch (Exception e) {
            num = 0;
        }
        setTag(num + 1);

        // 画笔
        TextPaint paint = getPaint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(0f);
        paint.setFakeBoldText(true);

        float cx = getWidth() * 0.5f;
        float cy = getHeight() * 0.2f;
        float radius;
        if (mRadius == 0f) {
            radius = Math.max(cx, cy) / 6;
        } else {
            radius = mRadius;
        }
        float angle = 360 / mCount;

        // init
//        paint.setColor(Color.WHITE);
        canvas.drawColor(mColorCanvas);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(mColorBackground);
        canvas.drawCircle(cx, cy, Math.min(cx, cy), paint);

        // 椭圆
        int length = num + mCount;
        for (int i = num; i < length; i++) {
            if (i == num) {
                if (num != 0) {
                    canvas.save();
                    canvas.rotate(angle * (i % mCount), cx, cx);
                }
//                int color = Color.parseColor("#FFA7A7A7");
                paint.setColor(mColorRound);
            } else {
                try {
                    float r = ((mColorRound >> 16) & 0xff) / 255.0f;
                    float g = ((mColorRound >> 8) & 0xff) / 255.0f;
                    float b = ((mColorRound) & 0xff) / 255.0f;
//                    float a = ((mColorRound >> 24) & 0xff) / 255.0f;
                    int a = (255 / 11) * (i - num);
                    int color = ((int) (a * 255.0f + 0.5f) << 24) |
                            ((int) (r * 255.0f + 0.5f) << 16) |
                            ((int) (g * 255.0f + 0.5f) << 8) |
                            (int) (b * 255.0f + 0.5f);
                    paint.setColor(color);
                } catch (Exception e) {
                }
            }
//            RectF rectF = new RectF(left, top, right, bottom);
//            canvas.drawRoundRect(rectF, rx, ry, paint);
            radius = radius * mRate;
            canvas.drawCircle(cx, cy, radius, paint);
            canvas.save();
            canvas.rotate(angle, cx, cx);
        }

        // delay
        postInvalidateDelayed(120);
    }

    /*************/

    @Keep
    public final void setCount(@NonNull int count) {
        this.mCount = count;
    }

    @Keep
    public final void setRate(@NonNull float rate) {
        this.mRate = rate;
    }

    @Keep
    public final void setRadius(@DimenRes int resId) {
        try {
            float dimension = getResources().getDimension(resId);
            this.mRadius = dimension;
        } catch (Exception e) {
        }
    }
}
