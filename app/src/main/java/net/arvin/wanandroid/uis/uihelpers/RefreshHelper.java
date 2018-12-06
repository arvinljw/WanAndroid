package net.arvin.wanandroid.uis.uihelpers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 15:56
 * Function：
 * Desc：
 */
public class RefreshHelper<T> implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private List<T> items;
    private BaseQuickAdapter<T, BaseViewHolder> adapter;
    private IRefreshPage refreshPage;

    public RefreshHelper(IRefreshPage refreshPage, SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, Class<? extends BaseQuickAdapter<T, BaseViewHolder>> adapterClass, int... adapterLayoutId) {
        this.refreshPage = refreshPage;
        this.refreshLayout = refreshLayout;
        this.recyclerView = recyclerView;

        Context context = recyclerView.getContext();
        refreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.primary));
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.divider))
                .build(recyclerView));
        this.items = new ArrayList<>();
        try {
            if (adapterLayoutId != null && adapterLayoutId.length > 0) {
                this.adapter = adapterClass.getConstructor(Integer.class, List.class).newInstance(adapterLayoutId[0], items);
            } else {
                this.adapter = adapterClass.getConstructor(List.class).newInstance(items);
            }
            adapter.setEnableLoadMore(false);
            recyclerView.setAdapter(this.adapter);
        } catch (Exception e) {
            throw new RuntimeException("Adapter's constructor must be Adapter(layoutId,List) or Adapter(List)");
        }
    }

    public void autoRefresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.removeItemDecorationAt(0);
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void loadSuccess(Response<List<T>> response) {
        items.clear();
        items.addAll(response.getData());
        refreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    public void loadError() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (refreshPage != null) {
            refreshPage.loadData();
        }
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener listener) {
        adapter.setOnItemClickListener(listener);
    }

    public T getItem(int position) {
        if (items == null || items.size() < position || position < 0) {
            return null;
        }
        return items.get(position);
    }

    public BaseQuickAdapter<T, BaseViewHolder> getAdapter() {
        return adapter;
    }
}
