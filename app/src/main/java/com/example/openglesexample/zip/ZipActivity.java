package com.example.openglesexample.zip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.openglesexample.R;
import com.example.openglesexample.util.Gl2Utils;

public class ZipActivity extends AppCompatActivity {

    private ZipAniView mAniView;
    private String nowMenu="assets/etczip/cc.zip";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        mAniView= (ZipAniView)findViewById(R.id.mAni);
        mAniView.setScaleType(Gl2Utils.TYPE_CENTERINSIDE);
        mAniView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAniView.isPlay()){
                    mAniView.setAnimation(nowMenu,50);
                    mAniView.start();
                }
            }
        });
        mAniView.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onStateChanged(int lastState, int nowState) {
                if(nowState==STOP){
                    if(!mAniView.isPlay()){
                        mAniView.setAnimation(nowMenu,50);
                        mAniView.start();
                    }
                }
            }
        });
    }

}
