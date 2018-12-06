package net.arvin.wanandroid.widgets;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.baselib.utils.DimenUtil;
import net.arvin.baselib.widgets.BasePopupWindow;
import net.arvin.itemdecorationhelper.ItemDecorationFactory;
import net.arvin.wanandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/30 16:40
 * Function：
 * Desc：
 */
public class ShowMorePopupWindow extends BasePopupWindow {
    private RecyclerView recyclerView;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;

    private List<String> items = new ArrayList<>();

    public ShowMorePopupWindow(Context context, View locationView) {
        super(context, locationView);
        recyclerView = mContent.findViewById(R.id.recycler_show_more);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_text, items) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_item, item);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.divider)).build(recyclerView));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_show_more;
    }

    @Override
    protected int getContentDefaultWidth() {
        return DimenUtil.getScreenWidth(mContent.getContext()) / 3;
    }

    public void setItems(List<String> items) {
        this.items.clear();
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public void onItemClickListener(BaseQuickAdapter.OnItemClickListener listener) {
        adapter.setOnItemClickListener(listener);
    }

}
