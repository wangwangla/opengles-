package com.example.openglesexample.learn;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.example.openglesexample.camera.CameraDrawer;
import com.example.openglesexample.camera.KitkatCamera;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Camera extends GLSurfaceView implements GLSurfaceView.Renderer {
    private CameInstance mCamera2;
    private MyCameraDrawer mCameraDrawer;
    private int cameraId=1;
    private Runnable mRunnable;

    public Camera(Context context) {
        super(context);
    }

    public Camera(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        mCamera2=new CameInstance();
        mCameraDrawer=new MyCameraDrawer(getResources());
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mCameraDrawer.onSurfaceCreated(gl10,eglConfig);
        if(mRunnable!=null){
            mRunnable.run();
            mRunnable=null;
        }
        mCamera2.open(cameraId);
        mCameraDrawer.setCameraId(cameraId);
        Point point=mCamera2.getPreviewSize();
        mCameraDrawer.setDataSize(point.x,point.y);
        mCamera2.setPreviewTexture(mCameraDrawer.getSurfaceTexture());
        mCameraDrawer.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        mCamera2.preview();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        mCameraDrawer.setViewSize(width,height);
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        mCameraDrawer.onDrawFrame(gl10);
    }
}
