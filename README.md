#  open gl es demo

一天一个或者一个周三个

## 手绘板

手绘板作为一个案例，是因为先想了解一下布局和安卓的绘制方式。
还有一个原因是之前的时候，不知道怎样绘制一个手指划过的线，比如游戏里面刚体的图形任意图形绘制。

- 知识点1：


布局

- 布局底部布局和底部动画

- 右边布局，以及动画

- 记录位置信息，绘制path


## 动画

最开始的原始动画和属性动画。传动动画包括：补间动画  和   帧动画

### 帧动画

- 帧动画就是一张一张的图片，将一张一张的进行播放

```xml
<?xml version="1.0" encoding="utf-8"?>
<animation-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:drawable="@drawable/a_0"
        android:duration="100" />
    <item
        android:drawable="@drawable/a_1"
        android:duration="100" />
    <item
        android:drawable="@drawable/a_2"
        android:duration="100" />
</animation-list>
```

- 显示

```java
ImageView animationImg1 = (ImageView) findViewById(R.id.animation1);
animationImg1.setImageResource(R.drawable.frame_anim1);
AnimationDrawable animationDrawable1 = (AnimationDrawable) animationImg1.getDrawable();
animationDrawable1.start();
```

### 补间动画

补间动画又可以分为四种形式，分别是 alpha（淡入淡出），translate（位移），scale（缩放大小），rotate（旋转）。


### 属性动画

属性动画   所有的补间动画，都可以使用属性动画完成

## 绘制

```java
package com.example.openglesexample.handdraw.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.jar.Attributes;

public class DrawView extends View {

    private int defaultColor =  Color.BLACK;
    private float defaultSize = 10f;
    private Paint mPaint = new Paint(){{
        setColor(defaultColor);
        setStrokeWidth(defaultSize);
        setStrokeJoin(Join.ROUND);
        setStrokeCap(Cap.ROUND);
        setStyle(Style.STROKE);

    }};
    private Path mPath = new Path();
    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attr){
        super(context,attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPath!=null){
            mPaint.setColor(defaultColor);
            mPaint.setStrokeWidth(defaultSize);
            canvas.drawPath(mPath,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
```