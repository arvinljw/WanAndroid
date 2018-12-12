package net.arvin.wanandroid.uis.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.baselib.widgets.MultiStatusView;
import net.arvin.baselib.widgets.MultiStatusViewHelper;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.NavigationInfoEntity;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.ArticlesRepo;
import net.arvin.wanandroid.uis.adapters.NavigationAdapter;
import net.arvin.wanandroid.uis.adapters.NavigationTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/13 17:21
 * Function：
 * Desc：
 */
public class NavigationFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener {
    private RecyclerView recyclerNavigation;
    private NavigationAdapter adapter;

    private RecyclerView recyclerTypes;
    private NavigationTypeAdapter typeAdapter;

    private List<NavigationInfoEntity> items = new ArrayList<>();
    private int currPos;
    private boolean shouldScroll;

    private MultiStatusViewHelper multiStatusViewHelper;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (shouldScroll) {
                return;
            }
            int firstVisibleItemPosition = getFirstVisibleItemPosition();
            if (currPos != firstVisibleItemPosition) {
                selectItem(firstVisibleItemPosition);
                scrollToPosition(recyclerTypes, currPos, true, false);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (shouldScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                shouldScroll = false;
                scrollToPosition(recyclerNavigation, currPos, false, true);
            }
        }
    };

    private RecyclerView.OnScrollListener onTypeScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (shouldScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                shouldScroll = false;
                scrollToPosition(recyclerTypes, currPos, false, true);
            }
        }
    };

    private int getFirstVisibleItemPosition() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerNavigation.getLayoutManager();
        if (layoutManager == null) {
            return 0;
        }
        return layoutManager.findFirstVisibleItemPosition();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_navigation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        recyclerNavigation = root.findViewById(R.id.recycler_navigation);
        recyclerTypes = root.findViewById(R.id.recycler_types);
        multiStatusViewHelper = new MultiStatusViewHelper((MultiStatusView) root.findViewById(R.id.multi_status_view), true);

        recyclerNavigation.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NavigationAdapter(items);
        recyclerNavigation.setAdapter(adapter);
        recyclerNavigation.addOnScrollListener(onScrollListener);

        recyclerTypes.setLayoutManager(new LinearLayoutManager(getActivity()));
        typeAdapter = new NavigationTypeAdapter(items);
        typeAdapter.setOnItemClickListener(this);
        recyclerTypes.setAdapter(typeAdapter);
        recyclerTypes.addOnScrollListener(onTypeScrollListener);

        loadData();
    }

    public void loadData() {
        ArticlesRepo.getNavigationInfo().observe(this, new ApiObserver<List<NavigationInfoEntity>>() {
            @Override
            public void onSuccess(Response<List<NavigationInfoEntity>> response) {
                items.clear();
                items.addAll(response.getData());
                if (items.size() > 0) {
                    currPos = 0;
                    items.get(currPos).setSelected(true);
                }
                typeAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                multiStatusViewHelper.showContent();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                multiStatusViewHelper.showRetryInList(MultiStatusViewHelper.hasData(items));
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                multiStatusViewHelper.showRetryInList(MultiStatusViewHelper.hasData(items));
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        selectItem(position);

        int firstVisibleItemPosition = getFirstVisibleItemPosition();
        if (currPos != firstVisibleItemPosition) {
            scrollToPosition(recyclerNavigation, currPos, true, true);
        }
    }

    private void scrollToPosition(RecyclerView recyclerView, int position, boolean needSmooth, boolean itemInScreenNeedScroll) {
        int firstItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
        int lastItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
        if (position < firstItem) {
            //在屏幕上方，直接滚上去就是顶部
            if (needSmooth) {
                recyclerView.smoothScrollToPosition(position);
            } else {
                recyclerView.scrollToPosition(position);
            }
        } else if (position <= lastItem) {
            if (itemInScreenNeedScroll) {
                //在屏幕中，直接滚动到相应位置的顶部
                int movePosition = position - firstItem;
                if (movePosition >= 0 && movePosition < recyclerView.getChildCount()) {
                    //粘性头部，会占据一定的top空间，所以真是的top位置应该是减去粘性header的高度
                    int top = recyclerView.getChildAt(movePosition).getTop();
                    if (needSmooth) {
                        recyclerView.smoothScrollBy(0, top);
                    } else {
                        recyclerView.scrollBy(0, top);
                    }
                }
            }
        } else {
            //在屏幕下方，需要西安滚动到屏幕内，在校验
            shouldScroll = true;
            if (needSmooth) {
                recyclerView.smoothScrollToPosition(position);
            } else {
                recyclerView.scrollToPosition(position);
            }
            currPos = position;
        }
    }

    private void selectItem(int position) {
        if (position < 0 || items.size() < position) {
            return;
        }
        items.get(currPos).setSelected(false);
        currPos = position;
        items.get(currPos).setSelected(true);
        typeAdapter.notifyDataSetChanged();
    }
}
