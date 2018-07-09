package com.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * ka
 * 08/11/2017
 */

public class UIHelper {

    private static final Logger LOGGER = Logger.getLogger(UIHelper.class);

    /**
     * get display size
     *
     * @param context: Context
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
     * @return: number of column
     */
    public static int calcGridColumn(Context context, int cellWidth) {
        Point displaySize = getDisplaySize(context);
        int width = displaySize.x;
        return width / cellWidth;
    }

    public static int getScreenOrientation(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            int orientation;
            if (display.getWidth() == display.getHeight()) {
                orientation = Configuration.ORIENTATION_SQUARE;
            } else {
                if (display.getWidth() < display.getHeight()) {
                    orientation = Configuration.ORIENTATION_PORTRAIT;
                } else {
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                }
            }
            return orientation;
        }
        return 0;
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
}
