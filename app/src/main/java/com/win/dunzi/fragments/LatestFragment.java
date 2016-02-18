package com.win.dunzi.fragments;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.win.dunzi.MyItemAnimation;
import com.win.dunzi.R;
import com.win.dunzi.adapters.ImageAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestFragment extends Fragment {

    private CoordinatorLayout mCoordinatorLayout;
    private ListView listView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout  mToolbarLayout;
    private ImageAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_latest, container, false);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar);

        //Bug点
        ((AppCompatActivity) getActivity()).setSupportActionBar( toolbar);
        mCoordinatorLayout= (CoordinatorLayout) view.findViewById(R.id.coordinator);
        mToolbarLayout= (CollapsingToolbarLayout) view.findViewById(R.id.toolbarLayout);
        mToolbarLayout.setTitle("猴赛雷");



        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            /**
             * 获得Item的偏移量
             * @param outRect
             * @param view
             * @param parent
             * @param state
             */
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                //设置分割线的高度
                //outRect.set(0, 2, 0, 2);
                //outRect.set(2, 2, 2, 2);
            }

            /**
             * 在Item绘制之前执行，会绘制在Item的下面
             * @param c
             * @param parent
             * @param state
             */
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                //设置分割线的颜色
                c.drawColor(Color.RED);
            }

            /**
             * 在Item绘制之后执行，绘制在Item的上层，会盖住Item
             * @param c
             * @param parent
             * @param state
             */
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                //c.drawColor(0x8800ff00);
            }

        });

        //瀑布流效果
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        try {
            String[] strings = getActivity().getAssets().list("images");
            mAdapter=new ImageAdapter(getActivity(),new ArrayList<>(Arrays.asList(strings)));
            recyclerView.setAdapter(mAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerView.setLayoutManager(manager);

        mAdapter.setListener(new ImageAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(View view, int position, String data) {
                System.out.println("点击了没有啊...............");
                mAdapter.remove(position);
            }
        });

        //动画，默认的动画系统
        MyItemAnimation animation=new MyItemAnimation();

        //删除动画的时候
        animation.setRemoveDuration(1000);
        animation.setAddDuration(1000);
        recyclerView.setItemAnimator(animation);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.coor,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
