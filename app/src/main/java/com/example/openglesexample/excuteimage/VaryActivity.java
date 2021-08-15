package com.example.openglesexample.excuteimage;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.example.openglesexample.R;

public class VaryActivity extends AppCompatActivity {
    GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vary);
        glSurfaceView = findViewById(R.id.mGLView);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new VaryRender(getResources()));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
