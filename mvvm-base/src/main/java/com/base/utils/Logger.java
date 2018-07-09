package com.base.utils;

import android.util.Log;

/**
 * ka
 * 08/11/2017
 */

public class Logger {

    private static boolean isDebug = false;

    private final String TAG;

    private Logger(Class<?> clazz) {
        TAG = clazz.getSimpleName();
    }

    public static void init(boolean isDebug) {
        Logger.isDebug = isDebug;
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    public void debug(Object msg) {
        if (isDebug) {
            if (msg != null) {
                Log.d(TAG, msg.toString());
            }
        }
    }

    public void debug(Object msg, Throwable tr) {
        if (isDebug) {
            if (msg != null) {
                Log.d(TAG, msg.toString(), tr);
            } else {
                Log.d(TAG, "Exception", tr);
            }
        }
    }

    public void info(Object msg) {
        if (isDebug) {
            if (msg != null) {
                Log.i(TAG, msg.toString());
            }
        }
    }

    public void info(Object msg, Throwable tr) {
        if (isDebug) {
            if (msg != null) {
                Log.i(TAG, msg.toString(), tr);
            } else {
                Log.i(TAG, "Exception", tr);
            }
        }
    }

    public void warn(Object msg) {
        if (isDebug) {
            if (msg != null) {
                Log.w(TAG, msg.toString());
            }
        }
    }

    public void warn(Object msg, Throwable tr) {
        if (isDebug) {
            if (msg != null) {
                Log.w(TAG, msg.toString(), tr);
            } else {
                Log.w(TAG, "Exception", tr);
            }
        }
    }

    public void error(Object msg) {
        if (isDebug) {
            if (msg != null) {
                Log.e(TAG, msg.toString());
            }
        }
    }

    public void error(Object msg, Throwable tr) {
        if (isDebug) {
            if (msg != null) {
                Log.e(TAG, msg.toString(), tr);
            } else {
                Log.e(TAG, "Exception", tr);
            }
        }
    }
}
