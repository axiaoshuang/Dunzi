package com.win.dunzi.adapters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.win.dunzi.R;

import java.util.List;

/**
 * Created by jash
 * Date: 16-1-6
 * Time: 上午11:26
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements SwipeDismissBehavior.OnDismissListener {
    private Context context;
    private List<String> list;
    private RecyclerView recyclerView;

    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) holder.text.getLayoutParams();
        SwipeDismissBehavior behavior = new SwipeDismissBehavior();
        behavior.setListener(this);
        params.setBehavior(behavior);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDismiss(View view) {
        ViewCompat.setAlpha(view, 1);
        View parent = (View) view.getParent();
        int position = recyclerView.getChildAdapterPosition(parent);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onDragStateChanged(int state) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            text = ((TextView) itemView.findViewById(android.R.id.text1));
        }
    }
}
