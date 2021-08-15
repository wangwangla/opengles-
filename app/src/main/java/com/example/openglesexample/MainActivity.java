package com.example.openglesexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.openglesexample.animation.Camera2Activity;
import com.example.openglesexample.blend.BlendActivity;
import com.example.openglesexample.camera.CameraActivity;
import com.example.openglesexample.egl.EGLActivity;
import com.example.openglesexample.eglex.EGLBackEnvActivity;
import com.example.openglesexample.excuteimage.VaryActivity;
import com.example.openglesexample.fbo.FBOActivity;
import com.example.openglesexample.light.LightActivity;
import com.example.openglesexample.meiyan.Camera3Activity;
import com.example.openglesexample.obj.ObjLoadActivity;
import com.example.openglesexample.obj2.ObjLoadActivity2;
import com.example.openglesexample.showimage.SGLViewActivity;
import com.example.openglesexample.square.FGLActivity;
import com.example.openglesexample.handdraw.HuiZhiActivity;
import com.example.openglesexample.vr.VrContextActivity;
import com.example.openglesexample.zip.ZipActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mList;
    private ArrayList<MenuBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList= (RecyclerView)findViewById(R.id.mList);
        mList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        data=new ArrayList<>();
        add("绘画板", HuiZhiActivity.class);
        add("EGL的使用 ", EGLActivity.class);
        add("绘制形体", FGLActivity.class);
        add("图片处理", SGLViewActivity.class);
        add("图形变换", VaryActivity.class);
        add("相机", CameraActivity.class);
        add("相机2 动画", Camera2Activity.class);
        add("相机3 美颜", Camera3Activity.class);
        add("压缩纹理动画", ZipActivity.class);
        add("FBO使用", FBOActivity.class);
        add("EGL后台处理", EGLBackEnvActivity.class);
        add("3D obj模型", ObjLoadActivity.class);
        add("obj+mtl模型", ObjLoadActivity2.class);
        add("VR效果", VrContextActivity.class);
        add("颜色混合", BlendActivity.class);
        add("光照", LightActivity.class);
        mList.setAdapter(new MenuAdapter());
    }

    private void add(String name,Class<?> clazz){
        MenuBean bean=new MenuBean();
        bean.name=name;
        bean.clazz=clazz;
        data.add(bean);
    }

    private class MenuBean{

        String name;
        Class<?> clazz;

    }

    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{


        @Override
        public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuHolder(getLayoutInflater().inflate(R.layout.item_button,parent,false));
        }

        @Override
        public void onBindViewHolder(MenuHolder holder, int position) {
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MenuHolder extends RecyclerView.ViewHolder{

            private Button mBtn;

            MenuHolder(View itemView) {
                super(itemView);
                mBtn= (Button)itemView.findViewById(R.id.mBtn);
                mBtn.setOnClickListener(MainActivity.this);
            }

            public void setPosition(int position){
                MenuBean bean=data.get(position);
                mBtn.setText(bean.name);
                mBtn.setTag(position);
            }
        }

    }

    @Override
    public void onClick(View view){
        int position= (int)view.getTag();
        MenuBean bean=data.get(position);
        startActivity(new Intent(this,bean.clazz));
    }
}
