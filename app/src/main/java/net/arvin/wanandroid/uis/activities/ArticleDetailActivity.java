package net.arvin.wanandroid.uis.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chad.library.adapter.base.BaseQuickAdapter;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.DimenUtil;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.nets.ApiObserver;
import net.arvin.wanandroid.nets.repositories.UserRepo;
import net.arvin.wanandroid.widgets.ShowMorePopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/21 17:46
 * Function：
 * Desc：
 */
public class ArticleDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    public static final String KEY_ARTICLE = "article";

    private SwipeRefreshLayout refreshLayout;
    private WebView webView;
    private String url;
    private ArticleEntity articleEntity;
    private List<String> showMores = new ArrayList<>();
    private ShowMorePopupWindow showMorePopupWindow;

    public static void open(Context context, String title, String url) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.KEY_TITLE, title);
        intent.putExtra(ArticleDetailActivity.KEY_URL, url);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    public static void open(Context context, ArticleEntity article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.KEY_TITLE, article.getTitle());
        intent.putExtra(ArticleDetailActivity.KEY_URL, article.getLink());
        intent.putExtra(ArticleDetailActivity.KEY_ARTICLE, article);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        refreshLayout = findViewById(R.id.refresh_layout);
        webView = findViewById(R.id.web_view);

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary));
        refreshLayout.setOnRefreshListener(this);
        initWebSettings();
        initWebClient();

        String title = getIntent().getStringExtra(KEY_TITLE);
        url = getIntent().getStringExtra(KEY_URL);
        articleEntity = getIntent().getParcelableExtra(KEY_ARTICLE);

        titleBar.getCenterTextView().setText(title);
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });
        if (articleEntity == null) {
            titleBar.getRightImageView().setVisibility(View.GONE);
        }

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    private void showMore() {
        if (showMorePopupWindow == null) {
            showMorePopupWindow = new ShowMorePopupWindow(this, findViewById(R.id.v_location));
            showMores.add(articleEntity.isCollect() ? "取消收藏" : "收藏");
            showMores.add("在浏览器中打开");
            showMorePopupWindow.setItems(showMores);
            showMorePopupWindow.onItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    showMorePopupWindow.dismiss();
                    if (position == 0) {
                        showMores.remove(0);
                        articleEntity.setCollect(!articleEntity.isCollect());
                        showMores.add(0, articleEntity.isCollect() ? "取消收藏" : "收藏");
                        showMorePopupWindow.setItems(showMores);
                        EventBus.getDefault().post(articleEntity);
                        collectionArticle();
                    } else if (position == 1) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
            });
        }
        showMorePopupWindow.showAtTopRight(DimenUtil.dp2px(this, -8), DimenUtil.dp2px(this, 8));
    }

    private void collectionArticle() {
        if (articleEntity.isCollect()) {
            UserRepo.collectArticle(articleEntity.getId()).observe(this, new ApiObserver<PageList<ArticleEntity>>() {
                @Override
                public void onSuccess(Response<PageList<ArticleEntity>> response) {
                    ToastUtil.showToast(ArticleDetailActivity.this, "收藏成功");
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    ToastUtil.showToast(ArticleDetailActivity.this, "收藏失败");
                }
            });
        } else {
            UserRepo.cancelCollectOriginArticle(articleEntity.getId()).observe(this, new ApiObserver<PageList<ArticleEntity>>() {
                @Override
                public void onSuccess(Response<PageList<ArticleEntity>> response) {
                    ToastUtil.showToast(ArticleDetailActivity.this, "取消收藏成功");
                }

                @Override
                public void onFailure(int code, String msg) {
                    super.onFailure(code, msg);
                    ToastUtil.showToast(ArticleDetailActivity.this, "取消收藏失败");
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        webView.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    protected void initWebClient() {
        webView.setWebViewClient(getWebViewClient());
    }

    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        };
    }

}
