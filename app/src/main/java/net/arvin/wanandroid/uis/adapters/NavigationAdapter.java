package net.arvin.wanandroid.uis.adapters;

import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.NavigationInfoEntity;
import net.arvin.wanandroid.uis.activities.ArticleDetailActivity;
import net.arvin.wanandroid.utils.Util;

import java.util.List;
import java.util.Random;

/**
 * Created by arvinljw on 2018/11/21 10:34
 * Function：
 * Desc：
 */
public class NavigationAdapter extends BaseQuickAdapter<NavigationInfoEntity, BaseViewHolder> {

    private Random random;

    public NavigationAdapter(@Nullable List<NavigationInfoEntity> data) {
        super(R.layout.item_navigation, data);
        random = new Random();
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationInfoEntity item) {
        helper.setText(R.id.tv_title, item.getName());
        FlowLayout layoutFlow = helper.getView(R.id.layout_flow);
        layoutFlow.removeAllViews();
        for (final ArticleEntity articleEntity : item.getArticles()) {
            View child = View.inflate(mContext, R.layout.layout_tag_navi, null);
            TextView textView = child.findViewById(R.id.tv_tag);
            textView.setText(articleEntity.getTitle());
            textView.setTextColor(Util.getRandomColor(random));
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity.open(mContext, articleEntity.getTitle(), articleEntity.getLink());
                }
            });

            layoutFlow.addView(child);
        }
    }
}
