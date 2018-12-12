package net.arvin.wanandroid;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;

import net.arvin.wanandroid.utils.SharePreferenceUtil;

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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        app = this;

        AppCompatDelegate.setDefaultNightMode(SharePreferenceUtil.isDarkStyle() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        Bugly.init(getApplicationContext(), "5406f1f177", BuildConfig.DEBUG);
    }

    public static App getApp() {
        return app;
    }
}
