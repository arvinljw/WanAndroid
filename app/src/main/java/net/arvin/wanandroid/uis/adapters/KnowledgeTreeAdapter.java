package net.arvin.wanandroid.uis.adapters;

import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.TreeEntity;
import net.arvin.wanandroid.uis.fragments.KnowledgeFragment;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 16:00
 * Function：
 * Desc：
 */
public class KnowledgeTreeAdapter extends BaseQuickAdapter<TreeEntity, BaseViewHolder> {

    public KnowledgeTreeAdapter(@Nullable List<TreeEntity> data) {
        super(R.layout.item_knowledge_tree, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, final TreeEntity item) {
        helper.setText(R.id.tv_knowledge_type, item.getName());
        FlowLayout layoutFlow = helper.getView(R.id.layout_flow);
        layoutFlow.removeAllViews();
        List<TreeEntity> children = item.getChildren();
        if (children == null || children.isEmpty()) {
            layoutFlow.setVisibility(View.GONE);
            return;
        }
        layoutFlow.setVisibility(View.VISIBLE);
        for (int i = 0; i < children.size(); i++) {
            TreeEntity tree = children.get(i);
            View child = View.inflate(mContext, R.layout.layout_tag_knowledge_tree, null);
            TextView textView = child.findViewById(R.id.tv_tag);

            String name = tree.getName();
            SpannableString content = new SpannableString(name);
            content.setSpan(new UnderlineSpan(), 0, name.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(content);
            final int pos = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KnowledgeFragment.startKnowledgeTreeTabActivity(mContext, item, pos);
                }
            });

            layoutFlow.addView(child);
        }
    }
}
