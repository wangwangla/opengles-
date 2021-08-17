package com.example.openglesexample.learn;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.openglesexample.filter.AFilter;
import com.example.openglesexample.filter.OesFilter;
import com.example.openglesexample.util.Gl2Utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyCameraDrawer implements GLSurfaceView.Renderer {
    private SurfaceTexture surfaceTexture;
    private float[] matrix=new float[16];
    private int width,height;
    private int dataWidth,dataHeight;
    private AFilter mOesFilter;
    private int cameraId=1;

    public MyCameraDrawer(Resources  resources){
        mOesFilter=new OesFilter(resources);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        int texture = createTextureID();
        surfaceTexture=new SurfaceTexture(texture);
        mOesFilter.create();
        mOesFilter.setTextureId(texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        setViewSize(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if(surfaceTexture!=null){
            surfaceTexture.updateTexImage();
        }
        mOesFilter.draw();
    }

    public void setDataSize(int dataWidth,int dataHeight){
        this.dataWidth=dataWidth;
        this.dataHeight=dataHeight;
        calculateMatrix();
    }

    public void setViewSize(int width,int height){
        this.width=width;
        this.height=height;
        calculateMatrix();
    }

    private void calculateMatrix(){
        Gl2Utils.getShowMatrix(matrix,this.dataWidth,this.dataHeight,this.width,this.height);
        if(cameraId==1){
            Gl2Utils.flip(matrix,true,false);
            Gl2Utils.rotate(matrix,90);
        }else{
            Gl2Utils.rotate(matrix,270);
        }
        mOesFilter.setMatrix(matrix);
    }

    public SurfaceTexture getSurfaceTexture(){
        return surfaceTexture;
    }

    public void setCameraId(int id){
        this.cameraId=id;
        calculateMatrix();
    }


    private int createTextureID(){
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }
}
