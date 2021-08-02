package com.example.openglesexample.square;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.view.View;

public abstract class Shape implements Renderer {
    protected View mView;
    public Shape(View mView){
        this.mView = mView;
    }

    public int loaderShader(int type,String shaperCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,shaperCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
