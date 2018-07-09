package com.base.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.base.utils.Logger;

import butterknife.ButterKnife;

/**
 * ka
 * 24/11/2017
 */

public abstract class AmazingBaseLayout extends RelativeLayout {

    private static final Logger K_LOGGER = Logger.getLogger(AmazingBaseLayout.class);

    public AmazingBaseLayout(Context context) {
        super(context);
        setLayout();
        ButterKnife.bind(this);
        updateUI();
    }

    public AmazingBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setLayout();
        ButterKnife.bind(this, this);
        updateUI();
    }

    protected int[] getStyleable() {
        return null;
    }

    protected abstract int getLayoutId();

    protected abstract void updateUI();

    protected void initDataFormStyleable(TypedArray typedArray) {

    }

    private void init(AttributeSet attrs) {
        if (getStyleable() != null && getStyleable().length > 0) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, getStyleable(), 0, 0);
            try {
                initDataFormStyleable(typedArray);
            } catch (Exception e) {
                e.printStackTrace();
                K_LOGGER.error("Cannot init view");
            } finally {
                typedArray.recycle();
            }
        }
    }

    private void setLayout() {
        if (getLayoutId() != 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(getLayoutId(), this, true);
        }
    }

    protected void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getContext(), clz);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}
