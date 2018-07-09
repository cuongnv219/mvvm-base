package com.base.ui.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.base.utils.KeyboardUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import dagger.android.support.AndroidSupportInjection;

/**
 * ka
 * 08/11/2017
 */

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment implements
        ViewTreeObserver.OnGlobalLayoutListener {

    protected BaseActivity activity;
    protected View rootView;
    private T viewDataBinding;
    private V viewModel;

    /**
     * @return layout resource id
     */
    protected abstract
    @LayoutRes
    int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    protected abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    protected abstract int getBindingVariable();

    /**
     * update screen
     *
     * @param savedInstanceState
     */
    protected abstract void updateUI(Bundle savedInstanceState);

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        viewModel = getViewModel();
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            try {
                viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false);
                if (viewDataBinding != null) {
                    rootView = viewDataBinding.getRoot();
                } else {
                    rootView = inflater.inflate(layoutId, container, false);
                }
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

                viewDataBinding.setVariable(getBindingVariable(), viewModel);
                viewDataBinding.executePendingBindings();

                updateUI(savedInstanceState);
            } catch (InflateException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            activity = (BaseActivity) context;
//            activity.onAttachFragment(this);
        } else {
            try {
                activity = (BaseActivity) getActivity();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        KeyboardUtil.hideSoftKeyboard(activity);
        activity = null;
        super.onDetach();
    }

    protected void toast(String msg, @StyleRes int style) {
        if (isAdded()) {
            StyleableToast.makeText(activity, msg, style).show();
        }
    }

    protected void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack) {
        String tag = fragmentClazz.getSimpleName();
        try {
            boolean isExisted = this.getChildFragmentManager().popBackStackImmediate(tag, 0);    // IllegalStateException
            if (!isExisted) {
                Fragment fragment;
                try {
                    fragment = fragmentClazz.newInstance();
                    if (args != null) {
                        fragment.setArguments(args);
                    }

                    FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
//                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.add(resId, fragment, tag);

                    if (addBackStack) {
                        transaction.addToBackStack(tag);
                    }
                    transaction.commitAllowingStateLoss();

                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected T getViewDataBinding() {
        return viewDataBinding;
    }

    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }

    protected void toast(String msg) {
        if (isAdded()) {
            Toast.makeText(rootView.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected void startActivity(Class<?> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    protected void startActivity(Class<?> clazz, Bundle b) {
        Intent i = new Intent(getContext(), clazz);
        i.putExtras(b);
        startActivity(i);
    }

    protected void startActivity(Class<?> clazz, String action) {
        Intent i = new Intent(getContext(), clazz);
        i.setAction(action);
        startActivity(i);
    }

    protected void startActivityNewTask(Class<?> clazz) {
        Intent i = new Intent(getContext(), clazz);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        Intent i = new Intent(activity, clazz);
        activity.startActivityForResult(i, requestCode);
    }

    protected void startActivityForResult(Class<?> clazz, int requestCode, Bundle b) {
        Intent i = new Intent(activity, clazz);
        i.putExtras(b);
        activity.startActivityForResult(i, requestCode);
    }
}
