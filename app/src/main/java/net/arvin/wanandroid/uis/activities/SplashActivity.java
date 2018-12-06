package net.arvin.wanandroid.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.facebook.shimmer.ShimmerFrameLayout;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.WeakHandler;
import net.arvin.wanandroid.R;

/**
 * Created by arvinljw on 2018/12/5 15:38
 * Function：
 * Desc：
 */
public class SplashActivity extends BaseActivity implements WeakHandler.IHandle {
    private ShimmerFrameLayout layoutShimmer;

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        WeakHandler handler = new WeakHandler(this);
        handler.sendEmptyMessageDelayed(0, 2000);
        layoutShimmer = findViewById(R.id.layout_shimmer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutShimmer.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        layoutShimmer.stopShimmer();
    }

    @Override
    public void handleMessage(Message msg) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
