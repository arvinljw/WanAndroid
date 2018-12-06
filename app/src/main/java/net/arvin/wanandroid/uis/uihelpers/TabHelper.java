package net.arvin.wanandroid.uis.uihelpers;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import net.arvin.wanandroid.uis.adapters.TabPageAdapter;

/**
 * Created by arvinljw on 2018/11/20 17:54
 * Function：
 * Desc：
 */
public class TabHelper {
    private static final int MAX_CACHE_SIZE = 4;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPageAdapter adapter;

    public TabHelper(TabLayout tabLayout, ViewPager viewPager, TabPageAdapter adapter) {
        this.tabLayout = tabLayout;
        this.viewPager = viewPager;
        this.adapter = adapter;

        if (adapter.getCount() <= MAX_CACHE_SIZE) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(MAX_CACHE_SIZE);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setCurrentPosition(int position) {
        viewPager.setCurrentItem(position);
    }

    public void setCurrentPosition(int position, boolean smoothScroll) {
        viewPager.setCurrentItem(position, smoothScroll);
    }

    public TabPageAdapter getAdapter() {
        return adapter;
    }

}
