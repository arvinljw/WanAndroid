package net.arvin.baselib.utils;

import android.util.Log;

import net.arvin.baselib.BuildConfig;

/**
 * Created by arvinljw on 2018/10/31 17:26
 * Function：
 * Desc：
 */
public class ALog {
    private static boolean debug = true;

    public static void init(boolean debug) {
        ALog.debug = debug;
    }

    public static void d(String tag, String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (debug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (debug) {
            Log.e(tag, msg);
        }
    }
}
