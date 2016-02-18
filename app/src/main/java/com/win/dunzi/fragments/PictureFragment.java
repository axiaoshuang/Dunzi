package com.win.dunzi.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.win.dunzi.PictureActivity;
import com.win.dunzi.R;
import com.win.dunzi.adapters.EnjoyAdapter;
import com.win.dunzi.entitys.EnjoyEntity;
import com.win.dunzi.utils.HttpUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener, Callback<EnjoyEntity> {


    private ListView listView;
    private SwipeRefreshLayout mRefreshLayout;
    private EnjoyAdapter mAdapter;
    private  int page=1;
    private boolean isBottom;
    private List<EnjoyEntity.ItemsEntity> items;
    View footView;//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);

        listView=(ListView)view.findViewById(R.id.picture_listView);
        mRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.picture_swipe);
        mRefreshLayout.setColorSchemeColors(Color.RED);
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter=new EnjoyAdapter(getContext());
        getInfoFromService(page);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        footView=LayoutInflater.from(getContext()).inflate(R.layout.enjoy_footerview,null);
        listView.addFooterView(footView);
        //TODO:小bug需要处理
        // footView.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onRefresh() {
        footView.setVisibility(View.GONE);
        page=1;
        mAdapter.clear();
        getInfoFromService(page);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isBottom){
            getInfoFromService(++page);
            System.out.println("page=" + page);
            footView.setVisibility(View.VISIBLE);
            isBottom=false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem+visibleItemCount==totalItemCount){
            isBottom=true;
        }
    }

    //点击详情页的跳转
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getContext(), PictureActivity.class);
        Bundle bundle=new Bundle();
        EnjoyEntity.ItemsEntity pictureItem = (EnjoyEntity.ItemsEntity) mAdapter.getItem(position);
        bundle.putSerializable("pictureItem",pictureItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void   getInfoFromService(int page){
        HttpUtils.getService().getImage(page).enqueue(this);
    }

    @Override
    public void onResponse(Response<EnjoyEntity> response, Retrofit retrofit) {
        if(response.code()==200){
            items = response.body().getItems();
            if(items!=null){
                mAdapter.addAll(items);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
    }
}
