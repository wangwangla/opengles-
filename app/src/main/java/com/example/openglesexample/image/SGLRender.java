package com.example.openglesexample.image;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;

import com.example.openglesexample.image.fliter.AFilter;
import com.example.openglesexample.image.fliter.ColorFilter;
import com.example.openglesexample.image.fliter.ContrastColorFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SGLRender implements GLSurfaceView.Renderer {

    private AFilter mFilter;
    private int width,height;
    private boolean refreshFlag=false;
    private EGLConfig config;
    private Bitmap bitmap;

    public SGLRender(SGLView sglView) {
        mFilter=new ContrastColorFilter(sglView.getContext(), ColorFilter.Filter.NONE);
    }
    public void setFilter(AFilter filter){
        refreshFlag=true;
        mFilter=filter;
        if(bitmap!=null){
            mFilter.setBitmap(bitmap);
        }
    }

    public void setImageBuffer(int[] buffer,int width,int height){
        bitmap= Bitmap.createBitmap(buffer,width,height, Bitmap.Config.RGB_565);
        mFilter.setBitmap(bitmap);
    }

    public void refresh(){
        refreshFlag=true;
    }

    public AFilter getFilter(){
        return mFilter;
    }

    public void setImage(Bitmap bitmap){
        this.bitmap=bitmap;
        mFilter.setBitmap(bitmap);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.config=config;
        mFilter.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width=width;
        this.height=height;
        mFilter.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if(refreshFlag&&width!=0&&height!=0){
            mFilter.onSurfaceCreated(gl, config);
            mFilter.onSurfaceChanged(gl,width,height);
            refreshFlag=false;
        }
        mFilter.onDrawFrame(gl);
    }
}
