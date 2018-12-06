package net.arvin.baselib.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import net.arvin.baselib.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by arvinljw on 2018/11/1 17:57
 * Function：
 * Desc：TitleBar组件的状态栏控制显示调整工具类
 */
public class StatusBarUtil {
    public static boolean supportTransparentStatusBar() {
        return OSUtils.isMiui()
                || OSUtils.isFlyme()
                || (OSUtils.isOppo() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 设置状态栏透明
     *
     * @param window
     */
    public static void transparentStatusBar(Window window) {
        if (OSUtils.isMiui() || OSUtils.isFlyme()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transparentStatusBarAbove21(window);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else if ((OSUtils.isOppo() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            transparentStatusBarAbove21(window);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            transparentStatusBarAbove21(window);
        }
    }

    @TargetApi(21)
    private static void transparentStatusBarAbove21(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置状态栏图标白色主题
     *
     * @param window
     */
    public static void setLightMode(Window window) {
        if (OSUtils.isMiui()) {
            setMIUIStatusBarDarkMode(window, false);
        } else if (OSUtils.isFlyme()) {
            setFlymeStatusBarDarkMode(window, false);
        } else if (OSUtils.isOppo()) {
            setOppoStatusBarDarkMode(window, false);
        } else {
            setStatusBarDarkMode(window, false);
        }
    }

    /**
     * 设置状态栏图片黑色主题
     *
     * @param window
     */
    public static void setDarkMode(Window window) {
        if (OSUtils.isMiui()) {
            setMIUIStatusBarDarkMode(window, true);
        } else if (OSUtils.isFlyme()) {
            setFlymeStatusBarDarkMode(window, true);
        } else if (OSUtils.isOppo()) {
            setOppoStatusBarDarkMode(window, true);
        } else {
            setStatusBarDarkMode(window, true);
        }
    }

    private static void setStatusBarDarkMode(Window window, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (darkMode) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    private static void setMIUIStatusBarDarkMode(Window window, boolean darkMode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Class<? extends Window> clazz = window.getClass();
            try {
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, darkMode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
            }
        }
        setStatusBarDarkMode(window, darkMode);
    }

    private static void setFlymeStatusBarDarkMode(Window window, boolean darkMode) {
        FlymeStatusBarUtils.setStatusBarDarkIcon(window, darkMode);
    }

    private static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;

    private static void setOppoStatusBarDarkMode(Window window, boolean darkMode) {
        int vis = window.getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (darkMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (darkMode) {
                vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            } else {
                vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            }
        }
        window.getDecorView().setSystemUiVisibility(vis);
    }

    /**
     * 设置状态栏颜色和透明度
     *
     * @param window
     * @param color
     * @param alpha
     */
    public static void setStatusBarColor(Window window, @ColorInt int color, int alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(calculateStatusColor(color, alpha));
        }
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 检测是否有虚拟导航栏
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 计算View Id
     *
     * @return
     */
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        } else {
            return UUID.randomUUID().hashCode();
        }
    }

    public static class OSUtils {
        private static final String ROM_MIUI = "MIUI";
        private static final String ROM_EMUI = "EMUI";
        private static final String ROM_FLYME = "FLYME";
        private static final String ROM_OPPO = "OPPO";
        private static final String ROM_SMARTISAN = "SMARTISAN";
        private static final String ROM_VIVO = "VIVO";
        private static final String ROM_QIKU = "QIKU";

        private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";
        private static final String KEY_VERSION_EMUI = "ro.build.version.emui";
        private static final String KEY_VERSION_OPPO = "ro.build.version.opporom";
        private static final String KEY_VERSION_SMARTISAN = "ro.smartisan.version";
        private static final String KEY_VERSION_VIVO = "ro.vivo.os.version";

        private static String sName;
        private static String sVersion;

        public static boolean isEmui() {
            return check(ROM_EMUI);
        }

        public static boolean isMiui() {
            return check(ROM_MIUI);
        }

        public static boolean isVivo() {
            return check(ROM_VIVO);
        }

        public static boolean isOppo() {
            return check(ROM_OPPO);
        }

        public static boolean isFlyme() {
            return check(ROM_FLYME);
        }

        public static boolean is360() {
            return check(ROM_QIKU) || check("360");
        }

        public static boolean isSmartisan() {
            return check(ROM_SMARTISAN);
        }

        public static String getName() {
            if (sName == null) {
                check("");
            }
            return sName;
        }

        public static String getVersion() {
            if (sVersion == null) {
                check("");
            }
            return sVersion;
        }

        private static boolean check(String rom) {
            if (sName != null) {
                return sName.equals(rom);
            }

            if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_MIUI))) {
                sName = ROM_MIUI;
            } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_EMUI))) {
                sName = ROM_EMUI;
            } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_OPPO))) {
                sName = ROM_OPPO;
            } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_VIVO))) {
                sName = ROM_VIVO;
            } else if (!TextUtils.isEmpty(sVersion = getProp(KEY_VERSION_SMARTISAN))) {
                sName = ROM_SMARTISAN;
            } else {
                sVersion = Build.DISPLAY;
                if (sVersion.toUpperCase().contains(ROM_FLYME)) {
                    sName = ROM_FLYME;
                } else {
                    sVersion = Build.UNKNOWN;
                    sName = Build.MANUFACTURER.toUpperCase();
                }
            }
            return sName.equals(rom);
        }

        private static String getProp(String name) {
            String line;
            BufferedReader input = null;
            try {
                Process p = Runtime.getRuntime().exec("getprop " + name);
                input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
                line = input.readLine();
                input.close();
            } catch (IOException ex) {
                return null;
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return line;
        }
    }

    public static class FlymeStatusBarUtils {
        private static Method mSetStatusBarColorIcon;
        private static Method mSetStatusBarDarkIcon;
        private static Field mStatusBarColorFiled;
        private static int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0;

        static {
            try {
                mSetStatusBarColorIcon = Activity.class.getMethod("setStatusBarDarkIcon", int.class);
            } catch (NoSuchMethodException e) {
            }
            try {
                mSetStatusBarDarkIcon = Activity.class.getMethod("setStatusBarDarkIcon", boolean.class);
            } catch (NoSuchMethodException e) {
            }
            try {
                mStatusBarColorFiled = WindowManager.LayoutParams.class.getField("statusBarColor");
            } catch (NoSuchFieldException e) {
            }
            try {
                Field field = View.class.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR");
                SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = field.getInt(null);
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            }
        }

        /**
         * 判断颜色是否偏黑色
         *
         * @param color 颜色
         * @param level 级别
         * @return
         */
        public static boolean isBlackColor(int color, int level) {
            int grey = toGrey(color);
            return grey < level;
        }

        /**
         * 颜色转换成灰度值
         *
         * @param rgb 颜色
         * @return　灰度值
         */
        public static int toGrey(int rgb) {
            int blue = rgb & 0x000000FF;
            int green = (rgb & 0x0000FF00) >> 8;
            int red = (rgb & 0x00FF0000) >> 16;
            return (red * 38 + green * 75 + blue * 15) >> 7;
        }

        /**
         * 设置状态栏字体图标颜色
         *
         * @param activity 当前activity
         * @param color    颜色
         */
        public static void setStatusBarDarkIcon(Activity activity, int color) {
            if (mSetStatusBarColorIcon != null) {
                try {
                    mSetStatusBarColorIcon.invoke(activity, color);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            } else {
                boolean whiteColor = isBlackColor(color, 50);
                if (mStatusBarColorFiled != null) {
                    setStatusBarDarkIcon(activity, whiteColor, whiteColor);
                    setStatusBarDarkIcon(activity.getWindow(), color);
                } else {
                    setStatusBarDarkIcon(activity, whiteColor);
                }
            }
        }

        /**
         * 设置状态栏字体图标颜色(只限全屏非activity情况)
         *
         * @param window 当前窗口
         * @param color  颜色
         */
        public static void setStatusBarDarkIcon(Window window, int color) {
            try {
                setStatusBarColor(window, color);
                if (Build.VERSION.SDK_INT > 22) {
                    setStatusBarDarkIcon(window.getDecorView(), true);
                }
            } catch (Exception e) {
            }
        }

        /**
         * 设置状态栏字体图标颜色
         *
         * @param activity 当前activity
         * @param dark     是否深色 true为深色 false 为白色
         */
        public static void setStatusBarDarkIcon(Activity activity, boolean dark) {
            setStatusBarDarkIcon(activity, dark, true);
        }

        private static boolean changeMeizuFlag(WindowManager.LayoutParams winParams, String flagName, boolean on) {
            try {
                Field f = winParams.getClass().getDeclaredField(flagName);
                f.setAccessible(true);
                int bits = f.getInt(winParams);
                Field f2 = winParams.getClass().getDeclaredField("meizuFlags");
                f2.setAccessible(true);
                int meizuFlags = f2.getInt(winParams);
                int oldFlags = meizuFlags;
                if (on) {
                    meizuFlags |= bits;
                } else {
                    meizuFlags &= ~bits;
                }
                if (oldFlags != meizuFlags) {
                    f2.setInt(winParams, meizuFlags);
                    return true;
                }
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (Throwable e) {
            }
            return false;
        }

        /**
         * 设置状态栏颜色
         *
         * @param view
         * @param dark
         */
        private static void setStatusBarDarkIcon(View view, boolean dark) {
            int oldVis = view.getSystemUiVisibility();
            int newVis = oldVis;
            if (dark) {
                newVis |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                newVis &= ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (newVis != oldVis) {
                view.setSystemUiVisibility(newVis);
            }
        }

        /**
         * 设置状态栏颜色
         *
         * @param window
         * @param color
         */
        private static void setStatusBarColor(Window window, int color) {
            WindowManager.LayoutParams winParams = window.getAttributes();
            if (mStatusBarColorFiled != null) {
                try {
                    int oldColor = mStatusBarColorFiled.getInt(winParams);
                    if (oldColor != color) {
                        mStatusBarColorFiled.set(winParams, color);
                        window.setAttributes(winParams);
                    }
                } catch (IllegalAccessException e) {
                }
            }
        }

        /**
         * 设置状态栏字体图标颜色(只限全屏非activity情况)
         *
         * @param window 当前窗口
         * @param dark   是否深色 true为深色 false 为白色
         */
        public static void setStatusBarDarkIcon(Window window, boolean dark) {
            if (Build.VERSION.SDK_INT < 23) {
                changeMeizuFlag(window.getAttributes(), "MEIZU_FLAG_DARK_STATUS_BAR_ICON", dark);
            } else {
                View decorView = window.getDecorView();
                if (decorView != null) {
                    setStatusBarDarkIcon(decorView, dark);
                    setStatusBarColor(window, 0);
                }
            }
        }

        private static void setStatusBarDarkIcon(Activity activity, boolean dark, boolean flag) {
            if (mSetStatusBarDarkIcon != null) {
                try {
                    mSetStatusBarDarkIcon.invoke(activity, dark);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            } else {
                if (flag) {
                    setStatusBarDarkIcon(activity.getWindow(), dark);
                }
            }
        }
    }
}
