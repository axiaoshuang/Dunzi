package com.win.dunzi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * author：WangShuang
 * Date: 2015/12/29 11:23
 * email：m15046658245_1@163.com
 */
public class CircleTransfrom implements Transformation {
    @Override
    public Bitmap transform(Bitmap bitmap) {

        Bitmap result=Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);

        Paint paint=new Paint();
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP));
        new Canvas(result).drawCircle(bitmap.getWidth()/2,bitmap.getHeight()/2,bitmap.getWidth()/2,paint);

        //释放原图
        bitmap.recycle();
        return result;
    }

    @Override
    public String key() {
        return "circle";
    }
}
