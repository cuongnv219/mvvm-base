package com.base.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.base.R;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import dagger.android.AndroidInjection;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * ka
 * 08/11/2017
 */

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    public static final int ANIM_NONE = 0;
    public static final int ANIM_BOTTOM_TO_TOP = 1;
    public static final int ANIM_TOP_TO_BOTTOM = 2;
    public static final int ANIM_RIGHT_TO_LEFT = 3;
    public static final int ANIM_LEFT_TO_RIGHT = 4;
    public static final int ANIM_FADE_IN_FADE_OUT = 5;
    protected FragmentManager fragmentManager;
    private T viewDataBinding;
    private V viewModel;
    private ProgressDialog progressDialog;

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract V getViewModel();

    protected abstract int getBindingVariable();

    protected abstract void updateUI(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        performDataBinding();

        updateUI(savedInstanceState);
    }

    private void performDataBinding() {
        fragmentManager = getSupportFragmentManager();
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        viewDataBinding.setVariable(getBindingVariable(), viewModel);
        viewDataBinding.executePendingBindings();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public T getViewDataBinding() {
        return viewDataBinding;
    }

    protected void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack) {
        String tag = fragmentClazz.getSimpleName();
        try {
            boolean isExisted = fragmentManager.popBackStackImmediate(tag, 0);    // IllegalStateException
            if (!isExisted) {
                Fragment fragment;
                try {
                    fragment = fragmentClazz.newInstance();
                    if (args != null) {
                        fragment.setArguments(args);
                    }

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.add(resId, fragment, tag);

                    if (addBackStack) {
                        transaction.addToBackStack(tag);
                    }
                    transaction.commitAllowingStateLoss();

                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoading(String message) {
        try {
            if (progressDialog != null) {
                progressDialog.show();
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(message);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Using try catch to catch the case: The activity is not running but still show the dialog.
    }

    public void showLoading(int messageId) {
        showLoading(getString(messageId));
    }

    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void toast(String msg, @StyleRes int style) {
        StyleableToast.makeText(this, msg, style).show();
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void startActivity(Class<?> clazz, String action) {
        Intent i = new Intent(this, clazz);
        i.setAction(action);
        startActivity(i);
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent i = new Intent(this, cls);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void startActivity(Class<?> cls, int animationType) {
        startActivity(cls);
        startTransition(animationType);
    }

    public void finish(int animationType) {
        finish();
        startTransition(animationType);
    }

    public void startActivity(Intent intent, int animationType) {
        super.startActivity(intent);
        startTransition(animationType);
    }

    public void startActivityForResult(Intent intent, int requestCode, int animationType) {
        super.startActivityForResult(intent, requestCode);
        startTransition(animationType);
    }

    public void startTransition(int animationType) {
        switch (animationType) {
            case ANIM_BOTTOM_TO_TOP:
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
            case ANIM_TOP_TO_BOTTOM:
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case ANIM_RIGHT_TO_LEFT:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
            case ANIM_LEFT_TO_RIGHT:
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case ANIM_FADE_IN_FADE_OUT:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    /**
     * Finish activity with Go to bottom animation.
     */
    public void finishGoToBottom() {
        finish(ANIM_TOP_TO_BOTTOM);
    }

    /**
     * Finish activity with Go to right animation.
     */
    public void finishGoToRight() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Start activity with Go to top animation.
     *
     * @param intent Intent start activity.
     */
    public void startActivityFromBottom(Intent intent) {
        startActivity(intent, ANIM_BOTTOM_TO_TOP);
    }

    public void startActivityFromRight(Class<?> cls) {
        startActivity(new Intent(this, cls));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    protected void startActivityNewTask(Class<?> clazz) {
        Intent i = new Intent(this, clazz);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    protected void startActivityForResult(Class<?> clazz, int requestCode) {
        Intent i = new Intent(this, clazz);
        startActivityForResult(i, requestCode);
    }

    protected void startActivityForResult(Class<?> clazz, int requestCode, Bundle b) {
        Intent i = new Intent(this, clazz);
        i.putExtras(b);
        startActivityForResult(i, requestCode);
    }
}
