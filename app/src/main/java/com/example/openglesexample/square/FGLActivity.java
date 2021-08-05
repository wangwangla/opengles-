package com.example.openglesexample.square;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.openglesexample.R;

public class FGLActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_CHOOSE=0x0101;
    private FGLView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fgl);
        init();
    }

    private void init(){
        mGLView= findViewById(R.id.mGLView);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.mChange:
                Intent intent=new Intent(this,ChooseActivity.class);
                startActivityForResult(intent,REQ_CHOOSE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            mGLView.setShape((Class<? extends Shape>) data.getSerializableExtra("name"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
