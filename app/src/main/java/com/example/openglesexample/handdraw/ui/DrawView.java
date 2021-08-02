package com.example.openglesexample.handdraw.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
