package com.win.dunzi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.win.dunzi.adapters.pingLun1Adapter;
import com.win.dunzi.entitys.EnjoyEntity;
import com.win.dunzi.utils.CircleTransfrom;
import com.win.dunzi.utils.HttpUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class EnjoyActivity extends AppCompatActivity implements Callback<EnjoyEntity>, AbsListView.OnScrollListener {

    private ListView listView;

    private ImageView userIcon;
    private TextView usertName;
    private TextView context;
    private ImageView bigImage;
    private TextView haoxiao;
    private TextView pinglun;
    private TextView fenxiang;
    private pingLun1Adapter mAdapter;
    private int pid;
    private  int page=1;
    private boolean isBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enjoy);


        Intent intent = getIntent();
        EnjoyEntity.ItemsEntity itemsEntity = (EnjoyEntity.ItemsEntity) intent.getSerializableExtra("itemsEntity");

         pid = itemsEntity.getId();

        System.out.println("pid="+pid);

        listView = (ListView) findViewById(R.id.activity_enjoy_listView);
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_enjoy_header, null);

        userIcon = (ImageView) headerView.findViewById(R.id.header_enjoy_user_icon);
        usertName = (TextView) headerView.findViewById(R.id.header_enjoy_user_name);
        context = (TextView) headerView.findViewById(R.id.header_enjoy_content);


        bigImage = (ImageView) headerView.findViewById(R.id.header_enjoy_images);



        haoxiao = (TextView) headerView.findViewById(R.id.header_enjoy_up);
        pinglun = (TextView) headerView.findViewById(R.id.header_enjoy_comments_count);
        fenxiang = (TextView) headerView.findViewById(R.id.header_enjoy_share_count);


        if (itemsEntity.getUser() != null) {
            if (itemsEntity.getUser().getLogin() != null) {
                usertName.setText(itemsEntity.getUser().getLogin());
                Picasso.with(this).load(getIconURL(itemsEntity.getUser().getId(), itemsEntity.getUser().getIcon()))
                        .transform(new CircleTransfrom())
                        .into(userIcon);
                if (itemsEntity.getUser().getIcon() == "" || itemsEntity.getUser().getIcon() == null) {
                 userIcon.setImageResource(R.mipmap.default_users_avatar);
                }
            } else {
                usertName.setText("匿名用户");
                userIcon.setImageResource(R.mipmap.default_users_avatar);
            }
        }

        //获取屏幕的高度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;


        if (itemsEntity.getImage() == null) {
            bigImage.setVisibility(View.GONE);
        } else {
           bigImage.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(getImageURL(itemsEntity.getImage()))
                    .resize(widthPixels, 0)
                    .into(bigImage);
        }
        context.setText(itemsEntity.getContent());
        haoxiao.setText("好笑:" + itemsEntity.getVotes().getUp());
        pinglun.setText("评论:" + itemsEntity.getComments_count() );
        fenxiang.setText("分享:" + itemsEntity.getShare_count());

        getInfoFromService(pid, page);
        mAdapter=new pingLun1Adapter(this);
        listView.setAdapter(mAdapter);
        listView.setOnScrollListener(this);
        listView.addHeaderView(headerView);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public static String getImageURL(String image) {
        String url = "http://pic.qiushibaike.com/system/pictures/%s/%s/%s/%s";
        Pattern pattern = Pattern.compile("(\\d+)\\d{4}");
        Matcher matcher = pattern.matcher(image);
        matcher.find();
        //缺一个检测网络
        return String.format(url, matcher.group(1), matcher.group(), "small", image);
    }

    private static String getIconURL(long id, String icon) {
        String url = "http://pic.qiushibaike.com/system/avtnew/%s/%s/thumb/%s";
        return String.format(url, id / 10000, id, icon);
    }

    @Override
    public void onResponse(Response<EnjoyEntity> response, Retrofit retrofit) {
        List<EnjoyEntity.ItemsEntity> items = response.body().getItems();
        if(response.code()==200){
            if(items!=null){
                mAdapter.addAll(items);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }


    //listView 分页的滑动的监听事件
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isBottom){
            getInfoFromService(pid,++page);

            System.out.println("刷新了数据....");
            System.out.println("page="+page);
            isBottom=false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem+visibleItemCount==totalItemCount){
            //Toast.makeText(EnjoyActivity.this,"到底了",Toast.LENGTH_SHORT).show();
            isBottom=true;
        }
    }

    public void getInfoFromService(int id,int page){
        HttpUtils.getService().getPinglun(id,page).enqueue(this);
    }
}


