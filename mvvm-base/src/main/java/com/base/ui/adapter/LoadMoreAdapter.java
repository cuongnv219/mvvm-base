package com.base.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.base.R;
import com.base.ui.widget.AmazingRecyclerView;

import java.util.List;

/**
 * ka
 * 08/11/2017
 */

public abstract class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROGRESS = 2;
    public static final int VIEW_HEADER = 3;
    public OnClickListener onClickListener;
    public Context context;

    public List<T> mListItem;
    protected AmazingRecyclerView.OnItemClickListener mOnItemClickListener;

    public LoadMoreAdapter(Context context, List<T> mListItem, OnClickListener<T> onClickListener) {
        this.context = context;
        this.mListItem = mListItem;
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return mListItem.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
        } catch (Exception e) {
            return VIEW_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(getIdLayoutItem(), parent, false);
            vh = createItemViewHolder(v);
        } else if (viewType == VIEW_PROGRESS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_load_more, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else {
            bindItemViewHolder(holder, position);
            if (onClickListener != null) {
                holder.itemView.setOnClickListener(v -> onClickListener.onClick(mListItem.get(holder.getAdapterPosition())));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListItem == null ? 0 : mListItem.size();
    }

    public void setOnItemClickListener(AmazingRecyclerView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public List<T> getListItem() {
        return mListItem;
    }

    protected abstract int getIdLayoutItem();

    protected abstract void bindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder createItemViewHolder(View v);

    public interface OnClickListener<T> {
        void onClick(T t);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progress_bar);
        }
    }
}
