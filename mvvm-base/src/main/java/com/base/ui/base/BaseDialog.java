package com.base.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.base.R;

import dagger.android.support.AndroidSupportInjection;

/**
 * ka
 * 08/11/2017
 */

public abstract class BaseDialog<T extends ViewDataBinding, V extends BaseViewModel> extends DialogFragment {

    private BaseActivity activity;
    private V viewModel;
    private T dataBinding;

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract int getBindingVariable();

    protected abstract V getViewModel();

    protected abstract void updateUI();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            activity = (BaseActivity) context;
        } else {
            try {
                activity = (BaseActivity) getActivity();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //the content
        RelativeLayout root = new RelativeLayout(activity);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        //create the full screen dialog
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View view = dataBinding.getRoot();
        performDependencyInjection();

        viewModel = viewModel == null ? getViewModel() : viewModel;
        dataBinding.setVariable(getBindingVariable(), viewModel);
        dataBinding.executePendingBindings();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI();
    }

    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }

    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        show(transaction, tag);
    }

    public void dismissDialog(String tag) {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .remove(fragment)
                    .commitNow();
        }
    }

    public void dismissDialog(FragmentManager fragmentManager, String tag) throws IllegalStateException {
        try {
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment != null) {
                fragmentManager.beginTransaction()
                        .disallowAddToBackStack()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .remove(fragment)
                        .commitAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public T getViewDataBinding() {
        return dataBinding;
    }
}
