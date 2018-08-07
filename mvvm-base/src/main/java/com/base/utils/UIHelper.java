package com.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * ka
 * 08/11/2017
 */

public class UIHelper {

    /**
     * get display size
     *
     * @param context: Context
     *
     * @return: point(display size)
     */
    public static Point getDisplaySize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getSize(outSize);
        }
        return outSize;
    }

    /**
     * get display shorter side size
     *
     * @param context: Context
     *
     * @return size of display shorter side
     */
    public static int getDisplayShorterSideSize(Context context) {
        Point outSize = getDisplaySize(context);
        return outSize.x < outSize.y ? outSize.x : outSize.y;
    }

    /**
     * calculate column of grid with cell width, will use when display image in grid view
     *
     * @param context:   Context
     * @param cellWidth: cell width
     *
     * @return: number of column
     */
    public static int calcGridColumn(Context context, int cellWidth) {
        Point displaySize = getDisplaySize(context);
        int width = displaySize.x;
        return width / cellWidth;
    }

    public static int dpToPx(final Context context, final float dp) {
        // Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }

    public static int getScreenWidth(final Context context) {
        if (context == null) {
            return 0;
        }
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * Returns a valid DisplayMetrics object
     *
     * @param context valid context
     *
     * @return DisplayMetrics object
     */
    public static DisplayMetrics getDisplayMetrics(final Context context) {
        final WindowManager
                windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    public static int pxToDp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (px / metrics.density);
    }

    public static int spToPx(int sp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static int pxToSp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (int) metrics.scaledDensity;
    }

    public static int getScreenWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.heightPixels;
    }

    public static void setIconColor(ImageView iconHolder, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(iconHolder.getDrawable());
        DrawableCompat.setTint(wrappedDrawable, color);
        iconHolder.setImageDrawable(wrappedDrawable);
        iconHolder.invalidate();
    }

    /**
     * Gets a reference to a given drawable and prepares it for use with tinting through.
     *
     * @param resId the resource id for the given drawable
     *
     * @return a wrapped drawable ready fo use
     * with {@link android.support.v4.graphics.drawable.DrawableCompat}'s tinting methods
     *
     * @throws Resources.NotFoundException
     */
    public static Drawable getWrappedDrawable(Context context, @DrawableRes int resId) throws Resources.NotFoundException {
        return DrawableCompat.wrap(ResourcesCompat.getDrawable(context.getResources(), resId, null));
    }

    public static int getColor(Context context, @ColorRes int resId) throws Resources.NotFoundException {
        return ContextCompat.getColor(context, resId);
    }

    public static void removeGlobalLayoutObserver(View view, ViewTreeObserver.OnGlobalLayoutListener layoutListener) {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
    }
}
