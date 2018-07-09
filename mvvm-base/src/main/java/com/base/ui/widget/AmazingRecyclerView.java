package com.base.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;
import com.base.ui.adapter.LoadMoreAdapter;

import java.util.List;

/**
 * ka
 * 08/11/2017
 */

public class AmazingRecyclerView extends RelativeLayout {
    public RecyclerView recyclerViewActual;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private View layoutLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvMessageNoItem;
    private String mTextNoItem;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private boolean mIsExistHeader = false;
    private boolean loading = false;

    /*****************************************************/

    public AmazingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
        setLayout();
        initCompoundView();
        setListener();
    }

    /*****************************************************/

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.AmazingView, 0, 0);
        try {
            mTextNoItem = a.getString(R.styleable.AmazingView_textEmptyList);
        } catch (Exception e) {
            Log.e("Amazing View", "Cannot init view");
        } finally {
            a.recycle();
        }
    }

    protected void setLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_amazing_recycler_view, this, true);
    }

    private void initCompoundView() {
        layoutLoading = findViewById(R.id.layout_loading);
        recyclerViewActual = findViewById(R.id.rv_actual);
        recyclerViewActual.setHasFixedSize(true);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        tvMessageNoItem = findViewById(R.id.tv_message_no_item);

        if (mTextNoItem != null) {
            tvMessageNoItem.setText(mTextNoItem);
        }
    }

    private void setListener() {
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerViewActual.addOnScrollListener(listener);
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        swipeRefreshLayout.setColorSchemeResources(colorResIds);
    }

    /**
     * Set action to load more list.
     *
     * @param listener Load more listener.
     */
    public void setOnLoadMoreListener(final OnLoadMoreListener listener) {
        if (mLayoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager mLayoutManager = (LinearLayoutManager) this.mLayoutManager;
            recyclerViewActual.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (!loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = true;
                                listener.onLoadMore();
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * If you know the list loaded all item, call me to disable load more function.
     */
    public void disableLoadMore() {
        loading = true;
    }

    public void enableLoadMore() {
        loading = false;
    }

    /**
     * Set exist header or not.
     *
     * @param isExistHeader
     */
    public void setIsExistHeader(boolean isExistHeader) {
        this.mIsExistHeader = isExistHeader;
    }

    /**
     * Define layout for recycler view.
     *
     * @param layout
     */
    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerViewActual.setLayoutManager(layout);
        this.mLayoutManager = layout;
    }

    public void setLayoutManagerForGridLayoutWithHeader(Context context, final int numberColumn) {
        GridLayoutManager manager = new GridLayoutManager(context, numberColumn);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? numberColumn : 1;
            }
        });
        recyclerViewActual.setLayoutManager(manager);
        mLayoutManager = manager;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * Custom method setLayoutManager.
     * Only using for Linear layout and Grid layout.
     * If numberColumn < 2, recycle view will have list view layout
     * else it will have grid layout with column = numberColumn
     *
     * @param numberColumn
     */
    public void setLayoutManager(int numberColumn) {
        if (numberColumn < 2) {
            setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            setLayoutManager(new GridLayoutManager(getContext(), numberColumn));
        }
    }

    /**
     * Set action for pull and refresh list.
     *
     * @param listener
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        swipeRefreshLayout.setOnRefreshListener(listener);
        loading = false;
    }

    /**
     * Call me if the list have no action pull to refresh.
     */
    public void disableRefreshLayout() {
        swipeRefreshLayout.setEnabled(false);
    }

    public void enableRefreshLayout() {
        swipeRefreshLayout.setEnabled(true);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        recyclerViewActual.setAdapter(this.mAdapter);
    }

    /**
     * Set string content when the list is empty.
     *
     * @param stringId
     */
    public void setTextEmptyList(int stringId) {
        tvMessageNoItem.setText(stringId);
    }

    /**
     * Set string content when the list is empty.
     *
     * @param s
     */
    public void setTextEmptyList(String s) {
        tvMessageNoItem.setText(s);
    }

    /**
     * Refresh list to update newest data.
     */
    public void refreshList() {
        if (isNoItem()) {
            tvMessageNoItem.setVisibility(VISIBLE);
        } else {
            tvMessageNoItem.setVisibility(GONE);
            if (mAdapter instanceof LoadMoreAdapter) {
                List list = ((LoadMoreAdapter) mAdapter).getListItem();
                if (list != null && list.size() > 0 && list.get(list.size() - 1) == null) {
                    list.remove(list.size() - 1);
                }
            }
        }
        layoutLoading.setVisibility(GONE);
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    public void hideLoading() {
        if (layoutLoading != null) {
            layoutLoading.setVisibility(GONE);
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void finishRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Add new list item. Using it when use LoadMoreAdapter
     *
     * @param listItems
     */
    public void addListItems(List listItems) {
        if (mAdapter instanceof LoadMoreAdapter) {
            List list = ((LoadMoreAdapter) mAdapter).getListItem();
            if (list != null && list.size() > 0 && list.get(list.size() - 1) == null) {
                list.remove(list.size() - 1);
            }
            list.addAll(listItems);
            refreshList();
        }
    }

    private boolean isNoItem() {
        return mAdapter == null || (mAdapter.getItemCount() == 0 && !mIsExistHeader) || (mAdapter.getItemCount() == 1 && mIsExistHeader);
    }

    /**
     *
     */
    public void startLoading() {
        layoutLoading.setVisibility(View.VISIBLE);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
