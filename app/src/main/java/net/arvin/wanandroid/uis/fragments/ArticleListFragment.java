package net.arvin.wanandroid.uis.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.leonardoxh.livedatacalladapter.Resource;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.ArticlesRepo;
import net.arvin.wanandroid.nets.repositories.UserRepo;
import net.arvin.wanandroid.uis.activities.ArticleDetailActivity;
import net.arvin.wanandroid.uis.adapters.ArticleAdapter;
import net.arvin.wanandroid.uis.uihelpers.IRefreshPage;
import net.arvin.wanandroid.uis.uihelpers.RefreshLoadMoreHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by arvinljw on 2018/11/20 17:42
 * Function：
 * Desc：
 */
public class ArticleListFragment extends BaseFragment implements IRefreshPage, BaseQuickAdapter.OnItemClickListener {
    public static final int API_KNOWLEDGE_TREE = 0;
    public static final int API_PROJECT = 1;
    public static final int API_SEARCH_LIST = 2;
    public static final int API_COLLECTION_LIST = 3;
    private RefreshLoadMoreHelper<ArticleEntity> refreshLoadMoreHelper;
    private int cid;
    private String searchKey;

    private int whichApi;

    public ArticleListFragment() {
    }

    @SuppressLint("ValidFragment")
    public ArticleListFragment(int cid) {
        this(cid, API_KNOWLEDGE_TREE);
    }

    @SuppressLint("ValidFragment")
    public ArticleListFragment(int cid, int whichApi) {
        this.cid = cid;
        this.whichApi = whichApi;
    }

    @SuppressLint("ValidFragment")
    public ArticleListFragment(String searchKey) {
        this.searchKey = searchKey;
        this.whichApi = API_SEARCH_LIST;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_acticle_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_article);

        refreshLoadMoreHelper = new RefreshLoadMoreHelper<>(this, refreshLayout, recyclerView, ArticleAdapter.class);
        refreshLoadMoreHelper.setOnItemClickListener(this);
        refreshLoadMoreHelper.autoRefresh();
        ((ArticleAdapter) refreshLoadMoreHelper.getAdapter()).showCollection(whichApi != API_COLLECTION_LIST);

        EventBus.getDefault().register(this);
    }

    @Override
    public void loadData() {
        LiveData<Resource<Response<PageList<ArticleEntity>>>> responseLiveData = ArticlesRepo.getKnowledgeTreeArticles(refreshLoadMoreHelper.getCurrPage(), cid);
        if (whichApi == API_PROJECT) {
            responseLiveData = ArticlesRepo.getProjectArticles(refreshLoadMoreHelper.getCurrPage(), cid);
            refreshLoadMoreHelper.setFirstPage(0);
        } else if (whichApi == API_SEARCH_LIST) {
            responseLiveData = ArticlesRepo.getSearchArticles(refreshLoadMoreHelper.getCurrPage(), searchKey);
        } else if (whichApi == API_COLLECTION_LIST) {
            responseLiveData = UserRepo.getCollectionArticles(refreshLoadMoreHelper.getCurrPage());
        }
        responseLiveData.observe(this, new ApiObserver<PageList<ArticleEntity>>() {
            @Override
            public void onSuccess(Response<PageList<ArticleEntity>> response) {
                refreshLoadMoreHelper.loadSuccess(response);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refreshLoadMoreHelper.loadError();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                refreshLoadMoreHelper.loadError();
            }
        });
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
        refreshLoadMoreHelper.onDestroy();
        refreshLoadMoreHelper = null;
    }
}
