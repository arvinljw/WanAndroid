package net.arvin.baselib.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import net.arvin.baselib.R;
import net.arvin.baselib.utils.ALog;

/**
 * Created by arvinljw on 2018/12/11 11:21
 * Function：
 * Desc：多状态布局
 */
public class MultiStatusView extends FrameLayout {

    private View emptyView;
    private View retryView;
    private View loadingView;

    private OnClickListener retryClickListener;

    public MultiStatusView(@NonNull Context context) {
        this(context, null);
    }

    public MultiStatusView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusView);
        int emptyLayoutId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_emptyLayoutId, R.layout.msv_layout_empty);
        int retryLayoutId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_retryLayoutId, R.layout.msv_layout_retry);
        int loadingLayoutId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_loadingLayoutId, R.layout.msv_layout_loading);
        int retryBtnId = typedArray.getInt(R.styleable.MultiStatusView_msv_retryBtnId, R.id.msv_btn_retry);
        typedArray.recycle();

        if (getBackground() == null) {
            setBackgroundColor(Color.WHITE);
        }

        initView(emptyLayoutId, retryLayoutId, loadingLayoutId, retryBtnId);
    }

    private void initView(int emptyLayoutId, int retryLayoutId, int loadingLayoutId, int retryBtnId) {
        setEmptyView(emptyLayoutId);
        setRetryView(retryLayoutId);
        setLoadingView(loadingLayoutId);

        View retryBtn = retryView.findViewById(retryBtnId);
        if (notEmpty(retryBtn)) {
            retryBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (retryClickListener != null) {
                        retryClickListener.onClick(v);
                    }
                }
            });
        } else {
            ALog.e("MultiStatusView", "retryBtnId is not found!");
        }

        showContent();
    }

    private boolean notEmpty(Object obj) {
        return obj != null;
    }

    private boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public void setEmptyView(@LayoutRes int emptyViewId) {
        setEmptyView(View.inflate(getContext(), emptyViewId, null));
    }

    public void setRetryView(@LayoutRes int retryViewId) {
        setRetryView(View.inflate(getContext(), retryViewId, null));
    }

    public void setLoadingView(@LayoutRes int loadingViewId) {
        setLoadingView(View.inflate(getContext(), loadingViewId, null));
    }

    public void setEmptyView(View emptyViewParams) {
        if (notEmpty(emptyView)) {
            removeView(emptyView);
        }
        this.emptyView = emptyViewParams;
        addView(emptyView);
    }

    public void setRetryView(View retryViewParams) {
        if (notEmpty(retryView)) {
            removeView(retryView);
        }
        this.retryView = retryViewParams;
        addView(retryView);
    }

    public void setLoadingView(View loadingViewParam) {
        if (notEmpty(loadingView)) {
            removeView(loadingView);
        }
        this.loadingView = loadingViewParam;
        addView(loadingView);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public View getRetryView() {
        return retryView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void showContent() {
        setVisibility(View.GONE);
    }

    public void hideContent() {
        setVisibility(View.VISIBLE);
    }

    public void showEmpty() {
        hideContent();
        if (notEmpty(emptyView) && !isVisible(emptyView)) {
            emptyView.setVisibility(View.VISIBLE);
        }
        if (notEmpty(retryView) && isVisible(retryView)) {
            retryView.setVisibility(View.GONE);
        }
        if (notEmpty(loadingView) && isVisible(loadingView)) {
            loadingView.setVisibility(View.GONE);
        }
    }

    public void showLoading() {
        hideContent();
        if (notEmpty(emptyView) && isVisible(emptyView)) {
            emptyView.setVisibility(View.GONE);
        }
        if (notEmpty(retryView) && isVisible(retryView)) {
            retryView.setVisibility(View.GONE);
        }
        if (notEmpty(loadingView) && !isVisible(loadingView)) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    public void showRetry() {
        hideContent();
        if (notEmpty(emptyView) && isVisible(emptyView)) {
            emptyView.setVisibility(View.GONE);
        }
        if (notEmpty(retryView) && !isVisible(retryView)) {
            retryView.setVisibility(View.VISIBLE);
        }
        if (notEmpty(loadingView) && isVisible(loadingView)) {
            loadingView.setVisibility(View.GONE);
        }
    }

    public void setRetryClickListener(OnClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }

    public void onDestroy() {
        if (retryClickListener != null) {
            retryClickListener = null;
        }
    }
}
