package com.base.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ka
 * 21/02/2018
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(T item);
}
