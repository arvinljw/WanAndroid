package net.arvin.wanandroid.uis.adapters;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.NavigationInfoEntity;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/21 10:34
 * Function：
 * Desc：
 */
public class NavigationTypeAdapter extends BaseQuickAdapter<NavigationInfoEntity, BaseViewHolder> {

    public NavigationTypeAdapter(@Nullable List<NavigationInfoEntity> data) {
        super(R.layout.item_navigation_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationInfoEntity item) {
        TextView tvItem = helper.getView(R.id.tv_item);
        tvItem.setText(item.getName());
        tvItem.setSelected(item.isSelected());
    }
}
