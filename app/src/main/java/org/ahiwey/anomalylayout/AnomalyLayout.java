package org.ahiwey.anomalylayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 说明:不规则按钮（布局）的点击
 * 原理是点击空白像素时不消费MotionEvent#ACTION_DOWN事件
 * 继承的ConstraintLayout可以用你熟悉的LinearLayout和RelatedLayout代替
 * 项目: AnomalyLayout
 * 创建者: ahiwey
 * 创建时间: 2020/5/25 17:46
 * 来自: null
 */
public class AnomalyLayout extends ConstraintLayout {

    private int width = -1;
    private int height = -1;
    private Bitmap bitmap;

    public AnomalyLayout(Context context) {
        super(context);
    }

    public AnomalyLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnomalyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://只有按下时才继续往下运行
                break;
            case MotionEvent.ACTION_UP:
                performClick();
            default:
                return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        if (x < 0 || y < 0) {
            return false;
        }
        if (width == -1 || height == -1) {
            Drawable drawable = getBackground().getCurrent();
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            width = getWidth();
            height = getHeight();
        }
        if (bitmap == null || x >= width || y >= height) {
            return false;
        }
        // 直接bitmap.getPixel(x,y)可能会抛出IllegalArgumentException，x must be < bitmap.width()或y must
        // be < bitmap.height()
        //这里是取相对位置的像素
        int pixel = bitmap.getPixel((int) (bitmap.getWidth() * x / width),
                (int) (bitmap.getHeight() * y / height));
        if (Color.TRANSPARENT == pixel) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
