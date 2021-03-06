package com.example.openglesexample.zip;

import android.content.res.Resources;
import android.opengl.ETC1;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.openglesexample.animation.ZipPkmReader;
import com.example.openglesexample.filter.AFilter;
import com.example.openglesexample.util.Gl2Utils;

import java.nio.ByteBuffer;

public class ZipMulDrawer extends AFilter {

    private GLSurfaceView mView;
    private int timeStep=50;
    private boolean isPlay=false;
    private ByteBuffer emptyBuffer;
    private int width,height;
    private float[] SM= Gl2Utils.getOriginalMatrix();
    private int type=Gl2Utils.TYPE_CENTERINSIDE;
    public static final int TYPE=0x01;

    private int[] texture;

    private ZipPkmReader mPkmReader;
    private int mGlHAlpha;

    private StateChangeListener mStateChangeListener;

    public ZipMulDrawer(Resources mRes) {
        super(mRes);
        mPkmReader=new ZipPkmReader(mRes.getAssets());
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/pkm_mul.vert","shader/pkm_mul.frag");
        texture=new int[2];
        createEtcTexture(texture);
        setTextureId(texture[0]);

        mGlHAlpha= GLES20.glGetUniformLocation(mProgram,"vTextureAlpha");
    }

    @Override
    protected void onClear() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    protected void onSizeChanged(int width, int height) {
        emptyBuffer=ByteBuffer.allocateDirect(ETC1.getEncodedDataSize(width,height));
        this.width=width;
        this.height=height;
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    protected void onBindTexture() {
        ETC1Util.ETC1Texture t=mPkmReader.getNextTexture();
        ETC1Util.ETC1Texture tAlpha=mPkmReader.getNextTexture();
        Log.e("wuwang","is ETC null->"+(t==null));
        if(t!=null&&tAlpha!=null){
            Gl2Utils.getMatrix(SM,type,t.getWidth(),t.getHeight(),width,height);
            setMatrix(SM);
            onSetExpandData();
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType());
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D,0,0,GLES20.GL_RGB,GLES20
                    .GL_UNSIGNED_SHORT_5_6_5,t);
            GLES20.glUniform1i(mHTexture,getTextureType());

            GLES20.glActiveTexture(GLES20.GL_TEXTURE1+getTextureType());
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[1]);
            ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D,0,0,GLES20.GL_RGB,GLES20
                    .GL_UNSIGNED_SHORT_5_6_5,tAlpha);
            GLES20.glUniform1i(mGlHAlpha,1+getTextureType());
        }else{
            setMatrix(OM);
            onSetExpandData();
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType());
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D,0,0,GLES20.GL_RGB,GLES20
                    .GL_UNSIGNED_SHORT_5_6_5,new ETC1Util.ETC1Texture(width,height,emptyBuffer));
            GLES20.glUniform1i(mHTexture,getTextureType());

            GLES20.glActiveTexture(GLES20.GL_TEXTURE1+getTextureType());
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[1]);
            ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D,0,0,GLES20.GL_RGB,GLES20
                    .GL_UNSIGNED_SHORT_5_6_5,new ETC1Util.ETC1Texture(width,height,emptyBuffer));
            GLES20.glUniform1i(mGlHAlpha,1+getTextureType());
            isPlay=false;
        }
    }

    @Override
    public void setInt(int type, int... params) {
        if(type==TYPE){
            this.type=params[0];
        }
        super.setInt(type, params);
    }

    @Override
    public void draw() {
        if(time!=0){
            Log.e("wuwang","time-->"+(System.currentTimeMillis()-time));
        }
        time=System.currentTimeMillis();
        long startTime=System.currentTimeMillis();
        super.draw();
        long s=System.currentTimeMillis()-startTime;
        if(isPlay){
            if(s<timeStep){
                try {
                    Thread.sleep(timeStep-s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mView.requestRender();
        }else{
            changeState(StateChangeListener.PLAYING,StateChangeListener.STOP);
        }
    }

    private long time=0;

    public void setAnimation(GLSurfaceView view,String path,int timeStep){
        this.mView=view;
        this.timeStep=timeStep;
        mPkmReader.setZipPath(path);
    }

    public void start(){
        if(!isPlay) {
            stop();
            isPlay = true;
            changeState(StateChangeListener.STOP, StateChangeListener.START);
            mPkmReader.open();
            mView.requestRender();
        }
    }

    public boolean isPlay(){
        return isPlay;
    }

    public void stop(){
        if(mPkmReader!=null){
            mPkmReader.close();
        }
        isPlay=false;
    }

    public void setStateChangeListener(StateChangeListener listener){
        this.mStateChangeListener=listener;
    }

    private void changeState(int lastState,int nowState){
        if(this.mStateChangeListener!=null){
            this.mStateChangeListener.onStateChanged(lastState,nowState);
        }
    }

    private void createEtcTexture(int[] texture){
        //????????????
        GLES20.glGenTextures(2,texture,0);
        for (int i=0;i<texture.length;i++){
            //????????????
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[i]);
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //??????????????????S????????????????????????[1/2n,1-1/2n]???????????????????????????border??????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //??????????????????T????????????????????????[1/2n,1-1/2n]???????????????????????????border??????
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //??????????????????????????????????????????2D??????
        }
    }


}
