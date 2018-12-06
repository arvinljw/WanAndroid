package net.arvin.wanandroid.uis.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.entities.TreeEntity;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.ArticlesRepo;
import net.arvin.wanandroid.uis.adapters.TabPageAdapter;
import net.arvin.wanandroid.uis.uihelpers.IPageContent;
import net.arvin.wanandroid.uis.uihelpers.TabHelper;
import net.arvin.wanandroid.widgets.ErrorCallback;
import net.arvin.wanandroid.widgets.LoadingCallback;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 14:41
 * Function：
 * Desc：
 */
public class ProjectFragment extends BaseFragment implements IPageContent {
    private List<TreeEntity> data;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected int getContentView() {
        return R.layout.fragment_project;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tabLayout = root.findViewById(R.id.layout_tab);
        viewPager = root.findViewById(R.id.viewpager);

        loadData();
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadService.showCallback(LoadingCallback.class);
        loadData();
    }

    private void loadData() {
        ArticlesRepo.getProjects().observe(this, new ApiObserver<List<TreeEntity>>() {
            @Override
            public void onSuccess(Response<List<TreeEntity>> response) {
                loadService.showSuccess();
                data = response.getData();
                TabHelper tabHelper = new TabHelper(tabLayout, viewPager,
                        new TabPageAdapter<>(getChildFragmentManager(), data, ProjectFragment.this));
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                loadService.showCallback(ErrorCallback.class);
            }
        });
    }

    @Override
    public Fragment getItem(int position) {
        return new ArticleListFragment(data.get(position).getId(), ArticleListFragment.API_PROJECT);
    }
}
