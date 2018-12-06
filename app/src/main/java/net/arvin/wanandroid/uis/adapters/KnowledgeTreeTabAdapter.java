package net.arvin.wanandroid.uis.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.arvin.wanandroid.entities.TreeEntity;
import net.arvin.wanandroid.uis.uihelpers.IPageContent;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 17:33
 * Function：
 * Desc：
 */
public class KnowledgeTreeTabAdapter extends FragmentStatePagerAdapter {
    private List<TreeEntity> items;
    private IPageContent pageContent;

    public KnowledgeTreeTabAdapter(FragmentManager fm, List<TreeEntity> items, IPageContent pageContent) {
        super(fm);
        this.items = items;
        this.pageContent = pageContent;
    }

    @Override
    public Fragment getItem(int i) {
        if (pageContent == null) {
            return null;
        }
        return pageContent.getItem(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).getName();
    }

    @Override
    public int getCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }
}
