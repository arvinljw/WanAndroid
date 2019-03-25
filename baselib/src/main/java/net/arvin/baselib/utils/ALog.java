package net.arvin.baselib.utils;

import android.util.Log;

import com.google.gson.Gson;

import net.arvin.baselib.BuildConfig;

/**
 * Created by arvinljw on 2018/10/31 17:26
 * Function：
 * Desc：
 */
public class ALog {
    private static boolean debug = true;
    private static Gson sGson;

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

    public static void json(String tag, Object obj) {
        if (debug) {
            if (sGson == null) {
                sGson = new Gson();
            }
            d(tag, sGson.toJson(obj));
        }
    }
}
