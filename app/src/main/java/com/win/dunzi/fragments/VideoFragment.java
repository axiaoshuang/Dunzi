package com.win.dunzi.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.win.dunzi.R;
import com.win.dunzi.adapters.VideoAdapter;
import com.win.dunzi.entitys.VideoEntity;
import com.win.dunzi.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment implements Callback<VideoEntity>, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private List<VideoEntity.ItemsEntity> mList;
    private VideoAdapter mAdapter;
    private int page = 1;//分页的初始值
    private SwipeRefreshLayout swipe;
    public VideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.video_recycler);

        swipe=(SwipeRefreshLayout)view.findViewById(R.id.video_swipe);
        mList = new ArrayList<>();
        mAdapter = new VideoAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);

        //滑动到底部加载更富
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全选显示的ItemPosition
                    int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    int totalCount = layoutManager.getItemCount();
                    if (lastPosition == totalCount - 1) {
                        getInfoFromService(++page);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        swipe.setColorSchemeColors(Color.RED);
        swipe.setOnRefreshListener(this);

        getInfoFromService(page);
        return view;

    }

    @Override
    public void onResponse(Response<VideoEntity> response, Retrofit retrofit) {
        if (response.code() == 200) {
            List<VideoEntity.ItemsEntity> items = response.body().getItems();
            if(items!=null){
                mAdapter.addAll(items);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
    }

    //刷新的方法
    @Override
    public void onRefresh() {
        mAdapter.clear();
        getInfoFromService(1);
        mAdapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
        System.out.println("刷新没有");
    }

    public void getInfoFromService(int count) {
        HttpUtils.getService().getVideo(count).enqueue(this);
    }
}
