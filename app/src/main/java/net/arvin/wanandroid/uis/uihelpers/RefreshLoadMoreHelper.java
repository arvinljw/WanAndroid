package net.arvin.wanandroid.uis.uihelpers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 10:39
 * Function：
 * Desc：
 */
public class RefreshLoadMoreHelper<T> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private int firstPage = 0;
    private int currPage = firstPage;

    private SwipeRefreshLayout refreshLayout;

    private List<T> items;
    private BaseQuickAdapter<T, BaseViewHolder> adapter;
    private IRefreshPage refreshPage;

    public RefreshLoadMoreHelper(IRefreshPage refreshPage, SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, Class<? extends BaseQuickAdapter<T, BaseViewHolder>> adapterClass, int... adapterLayoutId) {
        this.refreshPage = refreshPage;
        this.refreshLayout = refreshLayout;

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
            adapter.setOnLoadMoreListener(this, recyclerView);
            adapter.setEmptyView(R.layout.layout_empty);
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

    public void loadSuccess(Response<PageList<T>> response) {
        if (firstPage + 1 == response.getData().getCurPage()) {
            items.clear();
        }
        items.addAll(response.getData().getData());
        adapter.setEnableLoadMore(response.getData().hasNext());
        if (currPage > firstPage) {
            adapter.loadMoreComplete();
        } else {
            refreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    }

    public void loadError() {
        if (currPage > firstPage) {
            adapter.loadMoreFail();
        } else {
            refreshLayout.setRefreshing(false);
        }
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    @Override
    public void onRefresh() {
        currPage = firstPage;
        if (refreshPage != null) {
            refreshPage.loadData();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        currPage++;
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

    public List<T> getItems() {
        return items;
    }

    public BaseQuickAdapter<T, BaseViewHolder> getAdapter() {
        return adapter;
    }

    public boolean isFirstPage() {
        return currPage == firstPage;
    }
}
