package com.win.dunzi.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.win.dunzi.EnjoyActivity;
import com.win.dunzi.R;
import com.win.dunzi.adapters.EnjoyAdapter;
import com.win.dunzi.entitys.EnjoyEntity;
import com.win.dunzi.entitys.ItemsEntity;
import com.win.dunzi.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnjoyFragment extends Fragment implements Callback<EnjoyEntity>, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {


    private ListView listView;
    private SwipeRefreshLayout mRefreshLayout;
    private EnjoyAdapter mAdapter;
    private int page = 1;
    private boolean isBottom;
    private List<EnjoyEntity.ItemsEntity> items;
    View footView;//
    List<EnjoyEntity.ItemsEntity> entityList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enjoy, container, false);
        listView = (ListView) view.findViewById(R.id.enjoy_listView);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.enjoy_swipe);
        mRefreshLayout.setColorSchemeColors(Color.RED);
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new EnjoyAdapter(getContext());
        getInfoFromService(page);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        footView = LayoutInflater.from(getContext()).inflate(R.layout.enjoy_footerview, null);
        listView.addFooterView(footView);
        //TODO:小bug需要处理
         footView.setVisibility(View.INVISIBLE);

        //----------------------------------------------------
        listView.setItemsCanFocus(true);

        //----------------------------------------------------


        //TODO:回显数据库的资料

        List<ItemsEntity> execute = new Select().from(ItemsEntity.class).execute();

         entityList=new ArrayList<>();

        for (int i = 0; i < execute.size(); i++) {
            int id = execute.get(i).pid;
            String content = execute.get(i).content;
            String image = execute.get(i).image;
            int comments_count = execute.get(i).comments_count;
            int share_count = execute.get(i).share_count;



            EnjoyEntity.ItemsEntity entitys=new EnjoyEntity.ItemsEntity();


            int pid = execute.get(i).user.pid;
            String login = execute.get(i).user.login;
            String icon = execute.get(i).user.icon;

            EnjoyEntity.ItemsEntity.UserEntity userEntity=new EnjoyEntity.ItemsEntity.UserEntity();
            userEntity.setIcon(icon);
            userEntity.setId(pid);
            userEntity.setLogin(login);


            int up = execute.get(i).votes.up;

            EnjoyEntity.ItemsEntity.VotesEntity votesEntity=new EnjoyEntity.ItemsEntity.VotesEntity();
            votesEntity.setUp(up);


            entitys.setId(id);
            entitys.setContent(content);
            entitys.setComments_count(comments_count);
            entitys.setShare_count(share_count);
            entitys.setImage(image);

            entitys.setUser(userEntity);
            entitys.setVotes(votesEntity);

            entityList.add(entitys);
        }
        mAdapter.addAll(entityList);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getInfoFromService(int count) {
        HttpUtils.getService().getEnjoy(count).enqueue(this);
    }

    @Override
    public void onResponse(Response<EnjoyEntity> response, Retrofit retrofit) {

        if (response.code() == 200) {
            items = response.body().getItems();
            if (items != null) {
                listView.addFooterView(footView);
                ActiveAndroid.beginTransaction();
                try {
                    for (int i = 0; i < items.size(); i++) {

                        ItemsEntity.UserEntity user = new ItemsEntity.UserEntity();
                        ItemsEntity.VotesEntity vote  =new ItemsEntity.VotesEntity();

                        if (items.get(i).getUser() != null) {
                            user.pid = items.get(i).getUser().getId();
                            user.icon = items.get(i).getUser().getIcon();
                            user.login = items.get(i).getUser().getLogin();
                        }

                        user.save();
                        vote.up=items.get(i).getVotes().getUp();
                        vote.save();

                        ItemsEntity dbItem = new ItemsEntity();

                        dbItem.image=items.get(i).getImage();
                        dbItem.pid = items.get(i).getId();
                        dbItem.content = items.get(i).getContent();
                        dbItem.comments_count = items.get(i).getComments_count();
                        dbItem.share_count = items.get(i).getShare_count();

                        dbItem.user = user;
                        dbItem.votes=vote;

                        dbItem.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
                mAdapter.addAll(items);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        listView.removeFooterView(footView);
        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if(network!=null&&network.isConnectedOrConnecting()){
            footView.setVisibility(View.GONE);
            page = 1;
            mAdapter.clear();
            getInfoFromService(page);
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            //TODO ..............
        }else {
            mAdapter.addAll(entityList);
            mAdapter.notifyDataSetChanged();
            footView.setVisibility(View.GONE);
            mRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isBottom) {
            getInfoFromService(++page);
            System.out.println("page=" + page);
            footView.setVisibility(View.VISIBLE);
            isBottom = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            isBottom = true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), EnjoyActivity.class);
        EnjoyEntity.ItemsEntity itemsEntity = (EnjoyEntity.ItemsEntity) mAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemsEntity", itemsEntity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    @Override
//    public void onClick(View v) {
//        //TODO:把软件分享给好友
//        OnekeyShare oks=new OnekeyShare();
//
//        oks.setText("爽爽App是一个比较吊的软件");
//        oks.setTitle("分享文本");
//        oks.setAddress("北京海淀区");
//
//
//        //OnekeyShare 又有一些自定义的功能
//        //列如，直接使用某一个平台进行分享，不需要用户进行选择
//        //每一个平台都有一个类定义 直接获取就可以了
//        // oks.setPlatform(SinaWeibo.NAME);
//
//        //不允许用户编辑 直接分享
//        //oks.setSilent(true);
//        oks.show(getContext());
//    }
}
