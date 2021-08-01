package com.example.openglesexample.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.example.openglesexample.R;
import com.example.openglesexample.util.DpConvPx;

import java.util.Arrays;
import java.util.List;

public class HuiZhiActivity extends AppCompatActivity {

    private boolean currentState = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hui_zhi);
        showBottomAni();
        showRightAni();
//        getApplicationContext()
    }

    private boolean isShow;
    private void showRightAni() {
        final List<View> views = Arrays.asList(
                findViewById(R.id.color1),
                findViewById(R.id.color2),
                findViewById(R.id.color3),
                findViewById(R.id.color4)
        );
        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShow = !isShow;
                if (isShow){
                    for (View temp : views) {
                        temp.animate().
                                translationY(0).
                                setDuration(300).
                                setInterpolator(new DecelerateInterpolator()).start();
                    }
                }else {
                    int x = 0;
                    for (View temp : views) {
                        x++;
                        temp.animate().
                                translationY(-DpConvPx.dp2px(getApplicationContext(), x * 70)).
                                setDuration(300).
                                setInterpolator(new DecelerateInterpolator()).start();
                    }
                }
            }
        });

    }

    private void showBottomAni() {
        final View viewById = findViewById(R.id.sizeBtn);
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int srcY = 0;
                int toY = 1;
                if (currentState){
                    srcY = 0;
                    toY = 1;
                    viewById.setVisibility(View.GONE);
                }else {
                    srcY = 1;
                    toY = 0;
                    viewById.setVisibility(View.VISIBLE);
//                    viewById.setVisibility(View.VISIBLE);
                }
                currentState = !currentState;
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.ABSOLUTE, 0f,
                        Animation.ABSOLUTE, 0f,
                        Animation.RELATIVE_TO_SELF, srcY,
                        Animation.RELATIVE_TO_SELF, toY
                );
                translateAnimation.setDuration(300);
                translateAnimation.setFillAfter(true);
                translateAnimation.setInterpolator(new DecelerateInterpolator());
                viewById.setAnimation(translateAnimation);
            }
        });
    }
}
