package net.arvin.wanandroid.uis.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.utils.Util;

/**
 * Created by arvinljw on 2018/11/30 15:10
 * Function：
 * Desc：
 */
public class AboutUsActivity extends BaseActivity {
    private ShimmerFrameLayout layoutShimmer;

    @Override
    protected int getContentView() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("关于我们");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("v" + Util.getVersionName());

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
}
