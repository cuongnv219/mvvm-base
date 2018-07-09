package com.base.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ka
 * 08/11/2017
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(Context context, CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }


    public static void show(Context context, int resText) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, resText, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showCenter(Context context, CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showLong(Context context, CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showLong(Context context, int resText) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, resText, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * if toast is showing -> cancel and show new toast
     *
     * @param context activity
     * @param text    to show
     * @param gravity where to show
     */
    public static void show(Context context, CharSequence text, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }
}
