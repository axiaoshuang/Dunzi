package com.win.dunzi.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.win.dunzi.R;
import com.win.dunzi.entitys.VideoEntity;
import com.win.dunzi.utils.CircleTransfrom;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author：WangShuang
 * Date: 2016/1/25 13:39
 * email：m15046658245_1@163.com
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> implements View.OnClickListener, MediaPlayer.OnPreparedListener {
    private Context mContext;
    private List<VideoEntity.ItemsEntity> list;

    private MediaPlayer mPlayer;

    public VideoAdapter(Context context, List<VideoEntity.ItemsEntity> list) {
        mContext = context;
        this.list = list;
        mPlayer = new MediaPlayer();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        holder.pic.setTag(holder);
        holder.pic.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoEntity.ItemsEntity entity = list.get(position);
        if (entity.getUser() != null) {
            if (entity.getUser().getLogin() != null) {
                holder.userName.setText(entity.getUser().getLogin());
                Picasso.with(mContext).load(getIconURL(entity.getUser().getId(), entity.getUser().getIcon()))
                        .transform(new CircleTransfrom())
                        .into(holder.userIcon);
                if (entity.getUser().getIcon() == null || entity.getUser().getIcon() == "") {
                    holder.userIcon.setImageResource(R.mipmap.default_users_avatar);
                }
            }
        } else {
            holder.userName.setText("匿名用户");
            holder.userIcon.setImageResource(R.mipmap.ic_launcher);
        }
        holder.content.setText(entity.getContent());
        holder.pic.setImageURI(Uri.parse(entity.getPic_url()));
        holder.haoxiao.setText("好笑 " + entity.getVotes().getUp());
        holder.pinglun.setText("评论 " + entity.getComments_count());
        holder.fenxiang.setText("分享 " + entity.getShare_count());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addAll(Collection<? extends VideoEntity.ItemsEntity> collection) {
        int size = list.size();
        list.addAll(collection);
        notifyItemRangeInserted(size, collection.size());
    }
    public void clear(){
        list.clear();
    }


    private VideoViewHolder lastHolder;

    @Override
    public void onClick(View v) {
        if (lastHolder != null) {
            lastHolder.pic.setVisibility(View.VISIBLE);
            lastHolder.play.setVisibility(View.VISIBLE);
        }
        VideoViewHolder tag = (VideoViewHolder) v.getTag();

        try {
            mPlayer.reset();//重置后在播放
            mPlayer.setOnPreparedListener(this);
            mPlayer.setDisplay(tag.surface.getHolder());
            mPlayer.setDataSource(list.get(tag.getAdapterPosition()).getLow_url());
            mPlayer.prepareAsync();
            tag.pic.setVisibility(View.INVISIBLE);
            tag.progressBar.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastHolder = tag;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        lastHolder.pic.setVisibility(View.INVISIBLE);
        lastHolder.progressBar.setVisibility(View.INVISIBLE);
        lastHolder.play.setVisibility(View.INVISIBLE);
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


    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView userIcon;
        private TextView userName;

        private TextView content;
        private SimpleDraweeView pic;
        private SurfaceView surface;
        private ImageView play;
        private ProgressBar progressBar;


        private TextView haoxiao;
        private TextView pinglun;
        private TextView fenxiang;

        public VideoViewHolder(View itemView) {
            super(itemView);

            userIcon = (ImageView) itemView.findViewById(R.id.video_item_userIcon);
            userName = (TextView) itemView.findViewById(R.id.video_item_userName);

            content = (TextView) itemView.findViewById(R.id.video_item_content);
            pic = (SimpleDraweeView) itemView.findViewById(R.id.video_item_pic);
            surface = (SurfaceView) itemView.findViewById(R.id.video_item_video);
            play = (ImageView) itemView.findViewById(R.id.video_item_play);
            progressBar = (ProgressBar) itemView.findViewById(R.id.video_item_progress);
            pic.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            pic.setAspectRatio(1);


            haoxiao = (TextView) itemView.findViewById(R.id.video_item_haoxiao);
            pinglun = (TextView) itemView.findViewById(R.id.video_item_pinglun);
            fenxiang = (TextView) itemView.findViewById(R.id.video_item_fenxiang);
        }
    }
}
