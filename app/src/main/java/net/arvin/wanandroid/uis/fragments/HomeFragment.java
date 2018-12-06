package net.arvin.wanandroid.uis.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.BannerEntity;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.ArticlesRepo;
import net.arvin.wanandroid.uis.activities.ArticleDetailActivity;
import net.arvin.wanandroid.uis.adapters.ArticleAdapter;
import net.arvin.wanandroid.uis.uihelpers.IRefreshPage;
import net.arvin.wanandroid.uis.uihelpers.RefreshLoadMoreHelper;
import net.arvin.wanandroid.utils.BannerImageLoader;
import net.arvin.wanandroid.widgets.ErrorCallback;
import net.arvin.wanandroid.widgets.LoadingCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 14:41
 * Function：
 * Desc：
 */
public class HomeFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, IRefreshPage {
    private RefreshLoadMoreHelper<ArticleEntity> refreshLoadMoreHelper;
    private View headerView;
    private Banner banner;
    private List<BannerEntity> bannerData;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_home);

        refreshLoadMoreHelper = new RefreshLoadMoreHelper<>(this, refreshLayout, recyclerView, ArticleAdapter.class);
        refreshLoadMoreHelper.setOnItemClickListener(this);
        refreshLoadMoreHelper.autoRefresh();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        refreshLoadMoreHelper.autoRefresh();
        loadService.showCallback(LoadingCallback.class);
    }

    @Override
    public void loadData() {
        boolean firstPage = refreshLoadMoreHelper.isFirstPage();
        if (firstPage) {
            ArticlesRepo.getBanners().observe(this, new ApiObserver<List<BannerEntity>>() {
                @Override
                public void onSuccess(Response<List<BannerEntity>> response) {
                    addBanner(response);
                }
            });
        }

        ArticlesRepo.getIndexArticles(refreshLoadMoreHelper.getCurrPage()).observe(this, new ApiObserver<PageList<ArticleEntity>>() {
            @Override
            public void onSuccess(Response<PageList<ArticleEntity>> response) {
                loadService.showSuccess();
                refreshLoadMoreHelper.loadSuccess(response);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refreshLoadMoreHelper.loadError();
                if (refreshLoadMoreHelper.getItems().size() == 0) {
                    loadService.showCallback(ErrorCallback.class);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                refreshLoadMoreHelper.loadError();
                if (refreshLoadMoreHelper.getItems().size() == 0) {
                    loadService.showCallback(ErrorCallback.class);
                }
            }
        });
    }

    private void addBanner(Response<List<BannerEntity>> response) {
        bannerData = response.getData();
        if (headerView == null) {
            headerView = View.inflate(getActivity(), R.layout.layout_banner, null);
            banner = headerView.findViewById(R.id.layout_banner);
            banner.setImageLoader(new BannerImageLoader());
            banner.setDelayTime(5000);
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    BannerEntity entity = bannerData.get(position);
                    ArticleDetailActivity.open(getActivity(), entity.getTitle(), entity.getUrl());
                }
            });
            refreshLoadMoreHelper.getAdapter().addHeaderView(headerView);
        }
        banner.setImages(BannerEntity.toImages(response.getData()));
        banner.start();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ArticleEntity item = refreshLoadMoreHelper.getItem(position);
        ArticleDetailActivity.open(getActivity(), item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionChange(ArticleEntity articleEntity) {
        for (ArticleEntity entity : refreshLoadMoreHelper.getItems()) {
            if (entity.getId() == articleEntity.getId()) {
                entity.setCollect(articleEntity.isCollect());
            }
        }
        refreshLoadMoreHelper.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
