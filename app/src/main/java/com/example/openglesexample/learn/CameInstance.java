package com.example.openglesexample.learn;


import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import com.example.openglesexample.camera.ICamera;
import com.example.openglesexample.camera.KitkatCamera;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameInstance {
    private Camera camera;
    private CameraSizeComparator sizeComparator;
    private Camera.Size picSize;
    private Camera.Size preSize;
    private Point mPicSize;
    private Point mPreSize;
    private ICamera.Config mConfig;

    public CameInstance(){
        this.mConfig=new ICamera.Config();
        mConfig.minPreviewWidth=720;
        mConfig.minPictureWidth=720;
        mConfig.rate=1.778f;
        sizeComparator=new CameraSizeComparator();
    }

    private boolean open(int cameId){
        //打开
        camera = Camera.open(cameId);
        //设置参数
        if (camera !=null){
            //设置预览   和    照片
            Camera.Parameters param=camera.getParameters();
            picSize=getPropPictureSize(
                    param.getSupportedPictureSizes(),
                    mConfig.rate,
                    mConfig.minPictureWidth);
            preSize=getPropPreviewSize(param.getSupportedPreviewSizes(),
                    mConfig.rate,
                    mConfig.minPreviewWidth);
            param.setPictureSize(picSize.width,picSize.height);
            param.setPreviewSize(preSize.width,preSize.height);
            camera.setParameters(param);
            Camera.Size pre=param.getPreviewSize();
            Camera.Size pic=param.getPictureSize();
            mPicSize=new Point(pic.height,pic.width);
            mPreSize=new Point(pre.height,pre.width);
            return true;
        }
        return false;
    }
    private Camera.Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.height >= minWidth) && equalRate(s, th)){
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;
        }
        return list.get(i);
    }

    private boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.03)
        {
            return true;
        }
        else{
            return false;
        }
    }


    private class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.height == rhs.height){
                return 0;
            }
            else if(lhs.height > rhs.height){
                return 1;
            }
            else{
                return -1;
            }
        }
    }


    private Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.height >= minWidth) && equalRate(s, th)){
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;
        }
        return list.get(i);
    }

    public void setPreviewTexture(SurfaceTexture texture){
        if(camera!=null){
            try {
                camera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setConfig(ICamera.Config config) {
        this.mConfig=config;
    }

    public boolean preview() {
        if(camera!=null){
            camera.startPreview();
        }
        return false;
    }


    public boolean switchTo(int cameraId) {
        close();
        open(cameraId);
        return false;
    }

    public boolean close() {
        if(camera!=null){
            try{
                camera.stopPreview();
                camera.release();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public Point getPreviewSize() {
        return mPreSize;
    }

    public Point getPictureSize() {
        return mPicSize;
    }

    public void setOnPreviewFrameCallback(final ICamera.PreviewFrameCallback callback) {
        if(callback!=null){
            camera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    callback.onPreviewFrame(data,mPreSize.x,mPreSize.y);
                }
            });
        }
    }

    public void addBuffer(byte[] buffer){
        if(camera!=null){
            camera.addCallbackBuffer(buffer);
        }
    }

    public void setOnPreviewFrameCallbackWithBuffer(final ICamera.PreviewFrameCallback callback) {
        if(camera !=null){
            Log.e("wuwang","Camera set CallbackWithBuffer");
            camera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    callback.onPreviewFrame(data,mPreSize.x,mPreSize.y);
                }
            });
        }
    }


}

