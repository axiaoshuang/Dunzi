package com.win.dunzi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    private Button mButton;
    private LinearLayout mLayout;
    private View mView;

    private  int mPointwidth;//两个圆点之间的距离

    private static int[] mImagesIds=new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    private ArrayList<ImageView>  imageList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);


        mViewPager= (ViewPager) findViewById(R.id.guide_viewPager);
        mButton= (Button) findViewById(R.id.bt_start);
        mLayout= (LinearLayout) findViewById(R.id.ll_point_group);
        mView=(View)findViewById(R.id.view_pointRed);

        initViews();
        mViewPager.setAdapter(new GuideAdapter());
        mViewPager.addOnPageChangeListener(this);
        mButton.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private  void initViews(){
        for (int i = 0; i < mImagesIds.length; i++) {
            ImageView images=new ImageView(GuideActivity.this);
            images.setBackgroundResource(mImagesIds[i]);
            imageList.add(images);
        }

        //引导页的小圆点

        for (int i = 0; i < mImagesIds.length; i++) {
            View point=new View(GuideActivity.this);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(10,10);
            point.setBackgroundResource(R.drawable.shap_point_gray);
            if(i>0){
                params.leftMargin=20;
            }
            point.setLayoutParams(params);
            mLayout.addView(point);
        }

        //获取视图树
        mLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mPointwidth=mLayout.getChildAt(1).getLeft()-mLayout.getChildAt(0).getLeft();
            }
        });
    }

    //滑动监听事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int len= (int) (mPointwidth*positionOffset+position*mPointwidth);
        RelativeLayout.LayoutParams params=  (RelativeLayout.LayoutParams)  mView.getLayoutParams();
        params.leftMargin=len;
        mView.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        if(position==mImagesIds.length-1){
            mButton.setVisibility(View.VISIBLE);
        }else {
            mButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //点击的跳转主页面处理
    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        sp.edit().putBoolean("is_user_guide_show",true).commit();

        //跳转主页
        startActivity(new Intent(GuideActivity.this,MainActivity.class));
        finish();
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImagesIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageList.get(position));
            return imageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
