package com.win.dunzi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener, Runnable {

    private RelativeLayout rela;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rela= (RelativeLayout) findViewById(R.id.splash_rela);
        imageView= (ImageView) findViewById(R.id.splash_imageView);

        new Handler().postDelayed(this,2000);
    }
    public void startAnimation(){

        AnimationSet set=new AnimationSet(false);

        //旋转动画
        RotateAnimation  rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);

        //缩放动画
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setFillAfter(true);

        // 透明度的动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);

        set.setAnimationListener(this);
        rela.startAnimation(set);
    }

    private  void jumpNextPage(){
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        boolean userGuide = preferences.getBoolean("is_user_guide_show", false);
        if(!userGuide){
             startActivity(new Intent(SplashActivity.this, GuideActivity.class));
             finish();
        }else {
             startActivity(new Intent(SplashActivity.this, MainActivity.class));
             finish();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void run() {
        jumpNextPage();
    }
}

