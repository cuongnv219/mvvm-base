package com.base.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import butterknife.ButterKnife;

/**
 * cuongnv
 * 3/22/18
 */

public abstract class BaseCustomLayout extends RelativeLayout {

    public BaseCustomLayout(Context context) {
        super(context);
        setLayout();
        ButterKnife.bind(this);
        initData();
    }

    public BaseCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setLayout();
        ButterKnife.bind(this, this);
        initData();
    }

    protected int[] getStyleableId() {
        return null;
    }

    protected void initDataFromStyleable(TypedArray a) {
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    private void init(AttributeSet attrs) {
        if (getStyleableId() != null && getStyleableId().length > 0) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, getStyleableId(), 0, 0);
            try {
                initDataFromStyleable(a);
            } catch (Exception e) {
                Log.e("BaseCustomLayout", "Cannot init view");
            } finally {
                a.recycle();
            }
        }
    }

    private void setLayout() {
        if (getLayoutId() != 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(getLayoutId(), this, true);
        }
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getContext(), cls);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int req, Bundle bundle) {
        Intent intent = new Intent(getContext(), cls);
        intent.putExtras(bundle);
        if (getContext() instanceof BaseActivity) {
            ((BaseActivity) getContext()).startActivityForResult(cls, req, bundle);
        }
    }

    protected void toast(String msg, @StyleRes int style) {
        StyleableToast.makeText(getContext(), msg, style).show();
    }
}