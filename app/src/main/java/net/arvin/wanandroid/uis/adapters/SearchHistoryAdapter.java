package net.arvin.wanandroid.uis.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.wanandroid.R;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/23 16:24
 * Function：
 * Desc：
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchHistoryAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item, item).addOnClickListener(R.id.img_delete);
    }
}
