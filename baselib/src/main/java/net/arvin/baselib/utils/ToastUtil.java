package net.arvin.baselib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by arvinljw on 2018/11/2 15:08
 * Function：
 * Desc：
 */
public class ToastUtil {
    public static void showToast(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int msgResId) {
        show(context, msgResId, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    public static void showToastLong(Context context, int msgResId) {
        show(context, msgResId, Toast.LENGTH_LONG);
    }

    private static void show(Context context, String msg, int during) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, msg, during).show();
    }

    private static void show(Context context, int resId, int during) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, resId, during).show();
    }
}
