package net.arvin.baselib.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by arvinljw on 2018/10/30 11:04
 * Function：
 * Desc：
 */
public class DimenUtil {
    private static int STATUS_BAR_HEIGHT = 0;

    public static int dp2px(Context context, float dp) {
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity * sp + 0.5f);
    }

    public static float px2dp(Context context, int px) {
        return px / context.getResources().getDisplayMetrics().density + 0.5f;
    }

    public static float px2sp(Context context, int px) {
        return px / context.getResources().getDisplayMetrics().scaledDensity + 0.5f;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        if (STATUS_BAR_HEIGHT == 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
            }
            if (STATUS_BAR_HEIGHT == 0) {
                int DEFAULT_STATUS_BAR_HEIGHT = 24;
                STATUS_BAR_HEIGHT = dp2px(context, DEFAULT_STATUS_BAR_HEIGHT);
            }
        }
        return STATUS_BAR_HEIGHT;
    }

    public static int[] getViewLocationInWindow(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        return position;
    }

    public static int[] getViewLocationOnScreen(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;
    }

    public static Rect getViewLocalVisibleRect(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect;
    }

    public static Rect getViewGlobalVisibleRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }
}
