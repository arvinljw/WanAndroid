package net.arvin.wanandroid.uis.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.arvin.baselib.utils.ImageLoader;
import net.arvin.wanandroid.R;
import net.arvin.wanandroid.entities.ArticleEntity;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 16:20
 * Function：
 * Desc：
 */
public class ArticleAdapter extends BaseQuickAdapter<ArticleEntity, BaseViewHolder> {
    private boolean showCollection = true;

    public ArticleAdapter(@Nullable List<ArticleEntity> data) {
        super(R.layout.item_home, data);
    }

    public void showCollection(boolean showCollection) {
        this.showCollection = showCollection;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleEntity item) {
        helper.setText(R.id.tv_title, deal(item.getTitle()));
        helper.setText(R.id.tv_time, item.getNiceDate());
        helper.setText(R.id.tv_desc, item.getDesc());
        helper.setGone(R.id.tv_desc, !TextUtils.isEmpty(item.getDesc()));

        helper.setText(R.id.tv_author, item.getAuthor());
        helper.setText(R.id.tv_chapter, item.getChapterName());
        if (showCollection) {
            helper.setImageResource(R.id.img_collection, item.isCollect() ? R.drawable.ic_collection : R.drawable.ic_uncollection);
        } else {
            helper.setGone(R.id.img_collection, false);
        }
        ImageView imgIcon = helper.getView(R.id.img_icon);
        ImageLoader.loadImage(mContext, item.getEnvelopePic(), imgIcon);
        helper.setGone(R.id.img_icon, !TextUtils.isEmpty(item.getEnvelopePic()));
    }

    private SpannableString deal(String title) {
        String keyStart = "<em class='highlight'>";
        String keyEnd = "</em>";
        if (!title.contains(keyStart) && !title.contains(keyEnd)) {
            return new SpannableString(title);
        }
        int start = title.indexOf(keyStart);
        int end = title.indexOf(keyEnd) - keyStart.length();
        title = title.replace(keyStart, "");
        title = title.replace(keyEnd, "");
        SpannableString sb = new SpannableString(title);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#F44336")), start, end, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }
}
