package net.arvin.wanandroid.uis.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.arvin.baselib.base.BaseFragment;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.entities.TreeEntity;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.ArticlesRepo;
import net.arvin.wanandroid.uis.activities.KnowledgeTreeTabActivity;
import net.arvin.wanandroid.uis.adapters.KnowledgeTreeAdapter;
import net.arvin.wanandroid.uis.uihelpers.IRefreshPage;
import net.arvin.wanandroid.uis.uihelpers.RefreshHelper;
import net.arvin.wanandroid.widgets.ErrorCallback;
import net.arvin.wanandroid.widgets.LoadingCallback;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 14:41
 * Function：
 * Desc：
 */
public class KnowledgeFragment extends BaseFragment implements IRefreshPage, BaseQuickAdapter.OnItemClickListener {
    private RefreshHelper<TreeEntity> refreshHelper;

    @Override
    protected int getContentView() {
        return R.layout.fragment_knowledge;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.refresh_layout);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_knowledge);

        refreshHelper = new RefreshHelper<>(this, refreshLayout, recyclerView, KnowledgeTreeAdapter.class);
        refreshHelper.autoRefresh();
        refreshHelper.setOnItemClickListener(this);
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadService.showCallback(LoadingCallback.class);
        loadData();
    }

    @Override
    public void loadData() {
        ArticlesRepo.getKnowledgeTree().observe(this, new ApiObserver<List<TreeEntity>>() {
            @Override
            public void onSuccess(Response<List<TreeEntity>> response) {
                loadService.showSuccess();
                refreshHelper.loadSuccess(response);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refreshHelper.loadError();
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                refreshHelper.loadError();
                loadService.showCallback(ErrorCallback.class);
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startKnowledgeTreeTabActivity(getActivity(), refreshHelper.getItem(position), 0);
    }

    public static void startKnowledgeTreeTabActivity(Context context, TreeEntity treeEntity, int position) {
        Intent intent = new Intent(context, KnowledgeTreeTabActivity.class);
        intent.putExtra(KnowledgeTreeTabActivity.KEY_DATA, treeEntity);
        intent.putExtra(KnowledgeTreeTabActivity.KEY_POSITION, position);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }
}
