package net.arvin.wanandroid.uis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.MultiStatusView;
import net.arvin.baselib.widgets.MultiStatusViewHelper;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.itemdecorationhelper.ItemDecorationFactory;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.HotKeyEntity;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.ArticlesRepo;
import net.arvin.wanandroid.uis.adapters.SearchHistoryAdapter;
import net.arvin.wanandroid.utils.SharePreferenceUtil;
import net.arvin.wanandroid.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by arvinljw on 2018/11/20 15:46
 * Function：
 * Desc：
 */
public class SearchActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    private TitleBar titleBar;
    private EditText edInput;
    private TextView tvHotTitle;
    private TextView tvSearchHistoryTitle;
    private FlowLayout flowLayout;
    private RecyclerView recyclerSearchHistory;
    private SearchHistoryAdapter adapter;
    private List<String> items = new ArrayList<>();
    private MultiStatusViewHelper multiStatusViewHelper;

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleBar = findViewById(R.id.title_bar);
        edInput = titleBar.getCenterCustomView().findViewById(R.id.ed_input);
        tvHotTitle = findViewById(R.id.tv_hot_title);
        tvSearchHistoryTitle = findViewById(R.id.tv_search_history_title);
        flowLayout = findViewById(R.id.layout_flow);
        recyclerSearchHistory = findViewById(R.id.recycler_search_history);

        multiStatusViewHelper = new MultiStatusViewHelper((MultiStatusView) findViewById(R.id.multi_status_view), true);

        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        recyclerSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchHistoryAdapter(items);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.bindToRecyclerView(recyclerSearchHistory);
        adapter.setEmptyView(R.layout.layout_empty_top);
        recyclerSearchHistory.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(getResources().getColor(R.color.divider))
                .build(recyclerSearchHistory));

        loadHotSearch();
        loadSearchHistory();
    }

    private void search() {
        String content = edInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast(this, "搜索内容不能为空");
            return;
        }
        toSearchArticleListActivity(content);
    }

    private void loadHotSearch() {
        ArticlesRepo.getHotKeys().observe(this, new ApiObserver<List<HotKeyEntity>>() {
            @Override
            public void onSuccess(Response<List<HotKeyEntity>> response) {
                multiStatusViewHelper.showContent();
                addHotKey(response.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                multiStatusViewHelper.showRetry();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                multiStatusViewHelper.showRetry();
            }
        });
    }

    private void addHotKey(List<HotKeyEntity> data) {
        Random random = new Random();
        for (final HotKeyEntity entity : data) {
            View child = View.inflate(this, R.layout.layout_tag_navi, null);
            TextView textView = child.findViewById(R.id.tv_tag);
            textView.setText(entity.getName());
            textView.setTextColor(Util.getRandomColor(random));
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toSearchArticleListActivity(entity.getName());
                }
            });

            flowLayout.addView(child);
        }
    }

    private void loadSearchHistory() {
        items.clear();
        items.addAll(SharePreferenceUtil.getSearchList());
        adapter.notifyDataSetChanged();
    }

    private void toSearchArticleListActivity(String searchKey) {
        saveSearchKey(searchKey);

        Intent intent = new Intent(this, SearchArticleListActivity.class);
        intent.putExtra(SearchArticleListActivity.KEY_SEARCH, searchKey);
        startActivity(intent);
    }

    private void saveSearchKey(String searchKey) {
        boolean exit = false;
        for (String item : items) {
            if (item.equals(searchKey)) {
                exit = true;
                break;
            }
        }
        if (exit) {
            items.remove(searchKey);
        }
        items.add(0, searchKey);
        adapter.notifyDataSetChanged();
        SharePreferenceUtil.saveSearchList(items);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        SharePreferenceUtil.saveSearchList(items);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        toSearchArticleListActivity(items.get(position));
    }
}
