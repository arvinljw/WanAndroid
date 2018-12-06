package net.arvin.wanandroid.uis.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.TreeEntity;
import net.arvin.wanandroid.uis.adapters.TabPageAdapter;
import net.arvin.wanandroid.uis.fragments.ArticleListFragment;
import net.arvin.wanandroid.uis.uihelpers.IPageContent;
import net.arvin.wanandroid.uis.uihelpers.TabHelper;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 16:45
 * Function：
 * Desc：
 */
public class KnowledgeTreeTabActivity extends BaseActivity implements IPageContent {
    public static final String KEY_DATA = "data";
    public static final String KEY_POSITION = "position";

    private TreeEntity treeEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_knowledge_tree_tab;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        TabLayout tabLayout = findViewById(R.id.layout_tab);
        ViewPager viewPager = findViewById(R.id.viewpager);

        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        treeEntity = getIntent().getParcelableExtra(KEY_DATA);
        int position = getIntent().getIntExtra(KEY_POSITION, 0);
        List<TreeEntity> children = treeEntity.getChildren();

        titleBar.getCenterTextView().setText(treeEntity.getName());
        TabHelper tabHelper = new TabHelper(tabLayout, viewPager, new TabPageAdapter<>(getSupportFragmentManager(), children, this));
        tabHelper.setCurrentPosition(position);
    }

    @Override
    public Fragment getItem(int position) {
        return new ArticleListFragment(treeEntity.getChildren().get(position).getId());
    }
}
