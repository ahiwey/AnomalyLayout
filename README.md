# AnomalyLayout
@[toc]
**本文参考自**[Android不规则点击区域详解](https://www.cnblogs.com/vanezkw/p/3806360.html)
**做了点击区域的优化**
## 1.原理
通过像素颜色（这里是透明）判断，拦截（不消费）MotionEvent.ACTION_DOWN事件
## 2.代码
```java
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
```
## 3.布局
```java
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorPrimary"
    tools:context=".MainActivity">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:id="@+id/cl_program_bottom"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent">

        <org.ahiwey.anomalylayout.AnomalyLayout
            android:id="@+id/al_left"
            android:layout_width="0dp"
            android:layout_height="40dp"
            android:background="@drawable/sl_al_left"
            android:clickable="true"
            android:focusable="true"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent">


            <Button
                android:id="@+id/btn_add"
                android:layout_width="24dp"
                android:layout_height="24dp"
                android:layout_marginStart="50dp"
                android:background="@drawable/ic_left_normal"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:gravity="center"
                android:text="@string/add"
                android:textColor="#FFFFFF"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toRightOf="@+id/btn_add"
                app:layout_constraintTop_toTopOf="parent" />
        </org.ahiwey.anomalylayout.AnomalyLayout>


        <org.ahiwey.anomalylayout.AnomalyLayout
            android:id="@+id/al_middle"
            android:layout_width="0dp"
            android:layout_height="40dp"
            android:background="@drawable/sl_al_middle"
            android:clickable="true"
            android:focusable="true"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <androidx.constraintlayout.widget.ConstraintLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent">

                <Button
                    android:id="@+id/btn_play"
                    android:layout_width="24dp"
                    android:layout_height="30dp"
                    android:background="@drawable/ic_middle_normal"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintTop_toTopOf="parent" />


                <TextView
                    android:layout_width="40dp"
                    android:layout_height="40dp"
                    android:layout_gravity="center"
                    android:duplicateParentState="true"
                    android:gravity="center"
                    android:text="@string/play"
                    android:textColor="@color/sl_tv_text"
                    android:textSize="16sp"
                    app:layout_constraintBottom_toBottomOf="parent"
                    app:layout_constraintLeft_toRightOf="@id/btn_play"
                    app:layout_constraintTop_toTopOf="parent" />
            </androidx.constraintlayout.widget.ConstraintLayout>

        </org.ahiwey.anomalylayout.AnomalyLayout>

        <org.ahiwey.anomalylayout.AnomalyLayout
            android:id="@+id/al_right"
            android:layout_width="0dp"
            android:layout_height="40dp"
            android:background="@drawable/sl_al_right"
            android:clickable="true"
            android:focusable="true"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent">


            <Button
                android:id="@+id/btn_edit"
                android:layout_width="24dp"
                android:layout_height="24dp"
                android:background="@drawable/ic_right_normal"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintRight_toLeftOf="@+id/tv_edit"
                app:layout_constraintTop_toTopOf="parent" />

            <TextView
                android:id="@+id/tv_edit"
                android:layout_width="40dp"
                android:layout_height="20dp"
                android:layout_marginEnd="60dp"
                android:gravity="center"
                android:text="@string/edit"
                android:textColor="#FFFFFF"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />


        </org.ahiwey.anomalylayout.AnomalyLayout>
    </androidx.constraintlayout.widget.ConstraintLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```
## 4.效果预览
#### 1.正常点击效果
左边布局
#### 2.子控件跟随父布局的点击
中间布局，在“播放”TextView子控件增加了android:duplicateParentState="true"属性
#### 3.无点击效果
右边布局，因为未在布局里添加android:clickable="true"、android:focusable="true"属性或代码里设置点击监听
## 5.说明与注意事项
1.继承Button的话就是不规则按钮
2.继承自布局的话就是不规则布局

