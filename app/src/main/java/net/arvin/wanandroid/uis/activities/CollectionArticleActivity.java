package net.arvin.wanandroid.uis.activities;

import android.os.Bundle;
import android.view.View;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.uis.fragments.ArticleListFragment;

/**
 * Created by arvinljw on 2018/11/30 14:57
 * Function：
 * Desc：
 */
public class CollectionArticleActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_collection_article;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("我的收藏");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.layout_articles,
                new ArticleListFragment(-1, ArticleListFragment.API_COLLECTION_LIST)).commit();
    }
}
