package net.arvin.wanandroid;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.kingja.loadsir.core.LoadSir;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import net.arvin.wanandroid.utils.SharePreferenceUtil;
import net.arvin.wanandroid.widgets.EmptyCallback;
import net.arvin.wanandroid.widgets.ErrorCallback;
import net.arvin.wanandroid.widgets.LoadingCallback;

/**
 * Created by arvinljw on 2018/11/22 16:37
 * Function：
 * Desc：
 */
public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        LoadSir.beginBuilder()
                .addCallback(new EmptyCallback())
                .addCallback(new ErrorCallback())
                .addCallback(new LoadingCallback())
                .commit();

        AppCompatDelegate.setDefaultNightMode(SharePreferenceUtil.isDarkStyle() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        Bugly.init(getApplicationContext(), "5406f1f177", BuildConfig.DEBUG);
    }

    public static App getApp() {
        return app;
    }
}
