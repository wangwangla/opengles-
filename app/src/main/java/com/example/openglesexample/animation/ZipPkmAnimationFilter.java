package com.example.openglesexample.animation;

import android.content.res.Resources;
import android.opengl.ETC1;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.util.Log;

import com.example.openglesexample.filter.AFilter;
import com.example.openglesexample.util.MatrixUtils;

import java.nio.ByteBuffer;

public class ZipPkmAnimationFilter extends AFilter {

    private boolean isPlay=false;
    private ByteBuffer emptyBuffer;
    private int width,height;
    private int type= MatrixUtils.TYPE_CENTERINSIDE;
    public static final int TYPE=0x01;

    private NoFilter mBaseFilter;

    private int[] texture;

    private ZipPkmReader mPkmReader;
    private int mGlHAlpha;

    public ZipPkmAnimationFilter(Resources mRes) {
        super(mRes);
        mBaseFilter=new NoFilter(mRes);
        mPkmReader=new ZipPkmReader(mRes.getAssets());
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/pkm_mul.vert","shader/pkm_mul.frag");
        texture=new int[2];
        createEtcTexture(texture);
        setTextureId(texture[0]);
        mGlHAlpha= GLES20.glGetUniformLocation(mProgram,"vTextureAlpha");
        mBaseFilter.create();
    }

    @Override
    protected void onClear() {

    }

    @Override
    protected void onSizeChanged(int width, int height) {
        emptyBuffer=ByteBuffer.allocateDirect(ETC1.getEncodedDataSize(width,height));
        this.width=width;
        this.height=height;
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mBaseFilter.setSize(width, height);
    }

    @Override
    public float[] getMatrix() {
        return mBaseFilter.getMatrix();
    }

    @Override
    protected void onBindTexture() {
        ETC1Util.ETC1Texture t=mPkmReader.getNextTexture();
        ETC1Util.ETC1Texture tAlpha=mPkmReader.getNextTexture();
        Log.e("wuwang","is ETC null->"+(t==null));
        if(t!=null&&tAlpha!=null){
            MatrixUtils.getMatrix(super.getMatrix(),MatrixUtils.TYPE_FITEND,t.getWidth(),t.getHeight(),width,height);
            MatrixUtils.flip(super.getMatrix(),false,true);
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
            if(mPkmReader!=null){
                mPkmReader.close();
                mPkmReader.open();
            }
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
    public void draw() {
        if(getTextureId()!=0){
            mBaseFilter.setTextureId(getTextureId());
            mBaseFilter.draw();
        }
        GLES20.glViewport(100,0,width/6,height/6);
        super.draw();
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void setInt(int type, int... params) {
        if(type==TYPE){
            this.type=params[0];
        }
        super.setInt(type, params);
    }

    public void setAnimation(String path){
        mPkmReader.setZipPath(path);
        mPkmReader.open();
    }


    @Override
    protected void finalize() throws Throwable {
        if(mPkmReader!=null){
            mPkmReader.close();
        }
        super.finalize();
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
