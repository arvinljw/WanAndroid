package net.arvin.wanandroid.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by arvinljw on 2018/11/22 15:13
 * Function：
 * Desc：
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        net.arvin.baselib.utils.ImageLoader.load(context, path, imageView, false, 0);
    }
}
