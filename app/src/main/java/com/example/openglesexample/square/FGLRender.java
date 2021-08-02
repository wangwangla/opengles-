package com.example.openglesexample.square;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FGLRender extends Shape{

    private Shape shape;
    private Class<? extends Shape> clazz= null;

    public FGLRender(View mView) {
        super(mView);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        GLES20.glClearColor(0.5f,0.5f,0.5f,1.0f);
        Log.e("wuwang","onSurfaceCreated");
        try {
            Constructor constructor=clazz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            shape= (Shape) constructor.newInstance(mView);
        } catch (Exception e) {
            e.printStackTrace();
//            shape=new Cube(mView);
        }
        shape.onSurfaceCreated(gl,eglConfig);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e("wuwang","onSurfaceChanged");
        GLES20.glViewport(0,0,width,height);
        shape.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        Log.e("xxx","onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        shape.onDrawFrame(gl10);
    }

    public void setShape(Class<? extends Shape> shape){
        this.clazz=shape;
    }

}
