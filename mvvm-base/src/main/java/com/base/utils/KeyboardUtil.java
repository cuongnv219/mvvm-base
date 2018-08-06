package com.base.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ka
 * 08/11/2017
 */

public class KeyboardUtil {

    public static void hideKeyBoardWhenClickOutSide(final View view, final Activity activity) {
        if (!(view instanceof EditText) && !((view instanceof Button))) {
            view.setOnTouchListener((v, event) -> {
//                    v.performClick();
                hideSoftKeyboard(activity);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyBoardWhenClickOutSide(innerView, activity);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideKeyBoardWhenClickOutSideText(final View view, final Activity activity) {
        if (!(view instanceof EditText) && !((view instanceof Button)) && !(view instanceof TextView)) {
            view.setOnTouchListener((v, event) -> {
//                    v.performClick();
                hideSoftKeyboard(activity);
                return false;
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyBoardWhenClickOutSideText(innerView, activity);
            }
        }
    }

    public static void showSoftKeyboard(final Context context, final EditText editText) {
        new Handler().postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }, 100);
    }

    public static void closeSoftKeyboard(Activity activity) {

        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }
}
