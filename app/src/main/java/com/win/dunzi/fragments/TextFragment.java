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

import com.win.dunzi.R;
import com.win.dunzi.TextActivity;
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
public class TextFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, Callback<EnjoyEntity> {
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
        View view=inflater.inflate(R.layout.fragment_text, container, false);

        listView=(ListView)view.findViewById(R.id.text_listView);
        mRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.text_swipe);


        mRefreshLayout.setColorSchemeColors(Color.RED);
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter=new EnjoyAdapter(getContext());
        getInfoFromService(page);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        footView=LayoutInflater.from(getContext()).inflate(R.layout.enjoy_footerview,null);
        listView.addFooterView(footView);
    }

    //加载更多 .............
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isBottom){
            footView.setVisibility(View.VISIBLE);
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

    //下拉刷新.......
    @Override
    public void onRefresh() {
        footView.setVisibility(View.GONE);
        page=1;
        mAdapter.clear();
        getInfoFromService(page);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    //单机事件的
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(getContext(), TextActivity.class);
        EnjoyEntity.ItemsEntity textItem  = (EnjoyEntity.ItemsEntity) mAdapter.getItem(position);
        Bundle bundle=new Bundle();
        bundle.putSerializable("textItem",textItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void  getInfoFromService(int page){
        HttpUtils.getService().getText(page).enqueue(this);
    }


    @Override
    public void onResponse(Response<EnjoyEntity> response, Retrofit retrofit) {
            if(response.code()==200){
                items=response.body().getItems();
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
