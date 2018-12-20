package net.arvin.wanandroid;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatDelegate;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import net.arvin.wanandroid.dao.ArticleDatabase;
import net.arvin.wanandroid.utils.SharePreferenceUtil;

/**
 * Created by arvinljw on 2018/11/22 16:37
 * Function：
 * Desc：
 */
public class App extends TinkerApplication {
    private static App app;
    private ArticleDatabase db;

    public App() {
        super(ShareConstants.TINKER_ENABLE_ALL, "net.arvin.wanandroid.AppLike");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        app = this;

        AppCompatDelegate.setDefaultNightMode(SharePreferenceUtil.isDarkStyle() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        db = Room.databaseBuilder(this, ArticleDatabase.class, "wanandroid").build();
    }

    public static App getApp() {
        return app;
    }

    public ArticleDatabase getDB() {
        return db;
    }
}
