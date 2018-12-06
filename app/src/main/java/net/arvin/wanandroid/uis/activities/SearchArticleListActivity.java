package net.arvin.wanandroid.uis.activities;

import android.os.Bundle;
import android.view.View;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.uis.fragments.ArticleListFragment;

/**
 * Created by arvinljw on 2018/11/23 16:13
 * Function：
 * Desc：
 */
public class SearchArticleListActivity extends BaseActivity {
    public static final String KEY_SEARCH = "search";

    @Override
    protected int getContentView() {
        return R.layout.activity_search_article_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String searchKey = getIntent().getStringExtra(KEY_SEARCH);
        titleBar.getCenterTextView().setText(searchKey);
        getSupportFragmentManager().beginTransaction().add(R.id.layout_content, new ArticleListFragment(searchKey)).commit();
    }
}
