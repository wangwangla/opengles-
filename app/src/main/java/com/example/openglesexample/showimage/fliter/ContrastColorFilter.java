package com.example.openglesexample.showimage.fliter;

import android.content.Context;
import android.opengl.GLES20;

public class ContrastColorFilter extends AFilter {

    private ColorFilter.Filter filter;

    private int hChangeType;
    private int hChangeColor;

    public ContrastColorFilter(Context context, ColorFilter.Filter filter) {
        super(context, "filter/half_color_vertex.sh", "filter/half_color_fragment.sh");
        this.filter=filter;
    }

    @Override
    public void onDrawSet() {
        GLES20.glUniform1i(hChangeType,filter.getType());
        GLES20.glUniform3fv(hChangeColor,1,filter.data(),0);
    }

    @Override
    public void onDrawCreatedSet(int mProgram) {
        hChangeType=GLES20.glGetUniformLocation(mProgram,"vChangeType");
        hChangeColor=GLES20.glGetUniformLocation(mProgram,"vChangeColor");
    }

    @Override
    public void setData(int index, int i) {
        super.setData(index, i);
        float[] data = filter.data();
        data[index] = i/10000.0F;
        System.out.println(index+"============="+data[index]);
    }
}
