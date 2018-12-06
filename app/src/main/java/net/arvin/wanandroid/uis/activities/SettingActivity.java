package net.arvin.wanandroid.uis.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tencent.bugly.beta.Beta;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ImageLoader;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.events.ChangeNightMode;
import net.arvin.wanandroid.entities.events.LogoutEvent;
import net.arvin.wanandroid.utils.DataCacheUtil;
import net.arvin.wanandroid.utils.SharePreferenceUtil;
import net.arvin.wanandroid.utils.Util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by arvinljw on 2018/11/30 15:09
 * Function：
 * Desc：
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ShimmerFrameLayout layoutShimmer;
    private AppCompatCheckBox cbNoImage;
    private AppCompatCheckBox cbDarkStyle;
    private TextView tvCacheSize;
    private TextView tvVersion;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        recreate();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("设置");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutShimmer = findViewById(R.id.layout_shimmer);
        tvCacheSize = findViewById(R.id.tv_cache_size);
        tvVersion = findViewById(R.id.tv_version);

        findViewById(R.id.tv_clear_cache).setOnClickListener(this);
        findViewById(R.id.tv_upgrade).setOnClickListener(this);
        findViewById(R.id.tv_feedback).setOnClickListener(this);

        cbNoImage = findViewById(R.id.cb_no_image);
        cbDarkStyle = findViewById(R.id.cb_dark_style);

        cbNoImage.setChecked(SharePreferenceUtil.isNoImage());
        cbDarkStyle.setChecked(SharePreferenceUtil.isDarkStyle());

        cbNoImage.setOnCheckedChangeListener(this);
        cbDarkStyle.setOnCheckedChangeListener(this);

        View tvLogout = findViewById(R.id.tv_logout);
        tvLogout.setVisibility(SharePreferenceUtil.getUser() == null ? View.GONE : View.VISIBLE);
        tvLogout.setOnClickListener(this);

        tvCacheSize.setText(DataCacheUtil.getTotalCacheSize(this));
        tvVersion.setText("v" + Util.getVersionName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear_cache:
                DataCacheUtil.cleanAllCache(this);
                ToastUtil.showToast(this, "清除缓存成功");
                tvCacheSize.setText(DataCacheUtil.getTotalCacheSize(this));
                break;
            case R.id.tv_upgrade:
                Beta.checkUpgrade();
                break;
            case R.id.tv_feedback:
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:17760330581@163.com"));
                startActivity(data);
                break;
            case R.id.tv_logout:
                SharePreferenceUtil.saveUser(null);
                EventBus.getDefault().post(new LogoutEvent());
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_no_image:
                ImageLoader.setIsNoImage(isChecked);
                SharePreferenceUtil.saveNoImage(isChecked);
                break;
            case R.id.cb_dark_style:
                SharePreferenceUtil.changeDarkStyle(isChecked);
                int nightMode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(nightMode);
                finish();
                EventBus.getDefault().post(new ChangeNightMode());
                break;
        }
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
}
