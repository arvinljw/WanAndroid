package net.arvin.wanandroid.uis.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.utils.WeakHandler;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.events.ChangeNightMode;
import net.arvin.wanandroid.uis.fragments.MainFragment;
import net.arvin.wanandroid.uis.fragments.SlidingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity implements MainFragment.IDrawerToggle, WeakHandler.IHandle {
    private MainFragment mainFragment;
    private SlidingFragment slidingFragment;

    private DrawerLayout layoutDrawer;

    private static final int MSG_QUIT = 0;
    private static final int TIME_QUIT = 2000;
    private WeakHandler handler;
    private boolean canQuit = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        handler = new WeakHandler(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.layout_main, mainFragment);
        slidingFragment = new SlidingFragment();
        fragmentTransaction.add(R.id.layout_navigation, slidingFragment);
        fragmentTransaction.commit();

        layoutDrawer = findViewById(R.id.layout_drawer);

    }

    @Override
    public void toggle() {
        @SuppressLint("RtlHardcoded")
        int leftDrawer = Gravity.LEFT;
        if (layoutDrawer.isDrawerOpen(leftDrawer)) {
            layoutDrawer.closeDrawer(leftDrawer);
        } else {
            layoutDrawer.openDrawer(leftDrawer);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        slidingFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeDarkMode(ChangeNightMode mode) {
        layoutDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                overridePendingTransition(0, 0);
            }
        }, 5);
    }

    @Override
    public void onBackPressed() {
        if (!canQuit) {
            canQuit = true;
            ToastUtil.showToast(this, "再按一次返回键退出～");
            handler.sendEmptyMessageDelayed(MSG_QUIT, TIME_QUIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 0) {
            canQuit = false;
        }
    }
}
