package com.win.dunzi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.win.dunzi.R;
import com.win.dunzi.entitys.EnjoyEntity;
import com.win.dunzi.utils.CircleTransfrom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author：WangShuang
 * Date: 2016/1/25 19:55
 * email：m15046658245_1@163.com
 */
public class EnjoyAdapter extends BaseAdapter {
    private Context mContext;
    private List<EnjoyEntity.ItemsEntity> mList;

    public EnjoyAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.enjoy_item, parent, false);
            convertView.setTag(new ViewHolde(convertView));
        }
        ViewHolde holde = (ViewHolde) convertView.getTag();
        EnjoyEntity.ItemsEntity enjoyEntity = mList.get(position);

        if (enjoyEntity.getUser() != null) {
            if (enjoyEntity.getUser().getLogin() != null) {
                holde.usertName.setText(enjoyEntity.getUser().getLogin());
                Picasso.with(mContext).load(getIconURL(enjoyEntity.getUser().getId(), enjoyEntity.getUser().getIcon()))
                        .transform(new CircleTransfrom())
                        .into(holde.userIcon);
                if (enjoyEntity.getUser().getIcon() == "" || enjoyEntity.getUser().getIcon() == null) {
                    holde.userIcon.setImageResource(R.mipmap.default_users_avatar);
                }
            } else {
                holde.usertName.setText("匿名用户");
                holde.userIcon.setImageResource(R.mipmap.default_users_avatar);
            }
        }
        holde.context.setText(enjoyEntity.getContent());
        holde.haoxiao.setText("好笑:" + enjoyEntity.getVotes().getUp());
        holde.pinglun.setText("评论:" + enjoyEntity.getComments_count() + "");
        holde.fenxiang.setText("分享:" + enjoyEntity.getShare_count() + "");

        if (enjoyEntity.getImage() == null) {
            holde.bigImage.setVisibility(View.GONE);
        } else {
            holde.bigImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(getImageURL(enjoyEntity.getImage()))
                    .resize(parent.getWidth(), 0)
                    .into(holde.bigImage);

        }
        return convertView;
    }


    public void addAll(Collection<? extends EnjoyEntity.ItemsEntity> collection) {
        mList.addAll(collection);
        notifyDataSetChanged();
    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolde {
        private ImageView userIcon;
        private TextView usertName;
        private TextView context;
        private ImageView bigImage;
        private TextView haoxiao;
        private TextView pinglun;
        private TextView fenxiang;

        public ViewHolde(View itemView) {
            userIcon = (ImageView) itemView.findViewById(R.id.enjoy_user_icon);
            usertName = (TextView) itemView.findViewById(R.id.enjoy_user_name);
            context = (TextView) itemView.findViewById(R.id.enjoy_content);
            bigImage = (ImageView) itemView.findViewById(R.id.enjoy_images);
            haoxiao = (TextView) itemView.findViewById(R.id.enjoy_up);
            haoxiao = (TextView) itemView.findViewById(R.id.enjoy_up);
            pinglun = (TextView) itemView.findViewById(R.id.enjoy_comments_count);
            fenxiang = (TextView) itemView.findViewById(R.id.enjoy_share_count);

        }
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
}
