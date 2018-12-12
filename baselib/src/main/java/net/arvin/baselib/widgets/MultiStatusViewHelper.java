package net.arvin.baselib.widgets;

import java.util.List;

/**
 * Created by arvinljw on 2018/12/11 16:48
 * Function：
 * Desc：
 */
public class MultiStatusViewHelper {
    private MultiStatusView multiStatusView;

    public MultiStatusViewHelper(MultiStatusView multiStatusView) {
        this(multiStatusView, false);
    }

    public MultiStatusViewHelper(MultiStatusView multiStatusView, boolean showLoading) {
        this.multiStatusView = multiStatusView;
        if (showLoading) {
            showLoading();
        }
    }

    public void showContent() {
        if (multiStatusView != null) {
            multiStatusView.showContent();
        }
    }

    public void showEmpty() {
        if (multiStatusView != null) {
            multiStatusView.showEmpty();
        }
    }

    public void showRetry() {
        if (multiStatusView != null) {
            multiStatusView.showRetry();
        }
    }

    public void showLoading() {
        if (multiStatusView != null) {
            multiStatusView.showLoading();
        }
    }

    public void showRetryInList(boolean hasData) {
        if (hasData) {
            showContent();
        } else {
            showRetry();
        }
    }

    public void showEmptyInList(boolean hasData) {
        if (hasData) {
            showContent();
        } else {
            showEmpty();
        }
    }

    public static <T> boolean hasData(List<T> list) {
        return list != null && list.size() > 0;
    }

}
