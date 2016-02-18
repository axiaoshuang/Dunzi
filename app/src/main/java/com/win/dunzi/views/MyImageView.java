package com.win.dunzi.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：WangShuang
 * Date: 2016/1/4 16:05
 * email：m15046658245_1@163.com
 */
public class MyImageView extends ImageView{
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if(drawable!=null){
            int ws = MeasureSpec.getSize(widthMeasureSpec);
            int hs=drawable.getIntrinsicHeight()*ws/drawable.getIntrinsicWidth();
            setMeasuredDimension(ws,hs);
        }else {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }

}
