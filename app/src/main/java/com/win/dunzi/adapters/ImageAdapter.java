package com.win.dunzi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.win.dunzi.R;

import java.io.IOException;
import java.util.List;


/**
 * author：WangShuang
 * Date: 2016/1/4 15:45
 * email：m15046658245_1@163.com
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> implements View.OnClickListener {


    private Context context;
    private List<String> list;

    private OnChildClickListener listener;
    private RecyclerView recyclerView;

    public ImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    public void add(int position ,String data){
        list.add(position, data);
        notifyItemInserted(position);

    }

    /**
     * 删除条目，
     * @param position 要删除条目的位置
     */
    public void remove(int position){
        list.remove(position);
        //notifyDataSetChanged();
        //带动画效果的刷新（移出）
        notifyItemRemoved(position);
        //带动画效果的刷新（添加）
        //notifyItemInserted(position);
        //带动画效果的刷新（改变）,默认是关闭的
        //notifyItemChanged(position);
        //带动画效果的刷新（移动）
        // notifyItemMoved();
        //从哪个位置开始添加多少个数据（区域性添加）
        //notifyItemRangeInserted();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        view.setOnClickListener(this);
        return new ImageViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public void onClick(View view){
        //在Item动画执行时不执行点击事件
        if (listener != null){
            //获取到点击的View在RecylerView中的位置
            int position = recyclerView.getChildAdapterPosition(view);
            //调用接口
            listener.onChildClick(view, position, list.get(position));
        }
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open
                    ("images/" + list.get(position)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.itenmImage.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView itenmImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            itenmImage = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }

    //定义点击事件的接口
    public interface OnChildClickListener{
        //参数根据需求来定义

        /**
         * @param view  视图
         * @param position  位置
         * @param data   数据
         */
        void onChildClick(View view, int position, String data);
    }

}

