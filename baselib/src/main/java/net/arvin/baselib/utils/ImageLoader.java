package net.arvin.baselib.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import net.arvin.baselib.R;

import java.io.File;

/**
 * Created by arvinljw on 2018/10/31 15:35
 * Function：
 * Desc：
 */
public class ImageLoader {
    private static final int DEFAULT_ERROR_RES_ID = R.drawable.image_error;
    private static final int DEFAULT_PLACE_RES_ID = R.drawable.image_place;

    private static boolean isNoImage = false;

    public static void setIsNoImage(boolean isNoImage) {
        ImageLoader.isNoImage = isNoImage;
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImage(Context context, String url, ImageView imageView, int... resIds) {
        load(context, url, imageView, false, 0, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImage(Context context, @DrawableRes @Nullable Integer url, ImageView imageView, int... resIds) {
        load(context, url, imageView, false, 0, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImage(Context context, File url, ImageView imageView, int... resIds) {
        load(context, url, imageView, false, 0, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImageCircle(Context context, String url, ImageView imageView, int... resIds) {
        load(context, url, imageView, true, 0, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImageCircle(Context context, @DrawableRes @Nullable Integer url, ImageView imageView, int... resIds) {
        load(context, url, imageView, true, 0, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImageCircle(Context context, File url, ImageView imageView, int... resIds) {
        load(context, url, imageView, true, 0, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImageCorner(Context context, String url, ImageView imageView, int cornerPx, int... resIds) {
        load(context, url, imageView, false, cornerPx, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImageCorner(Context context, @DrawableRes @Nullable Integer url, ImageView imageView, int cornerPx, int... resIds) {
        load(context, url, imageView, false, cornerPx, resIds);
    }

    /**
     * @param resIds {@link #load}
     */
    public static void loadImageCorner(Context context, File url, ImageView imageView, int cornerPx, int... resIds) {
        load(context, url, imageView, false, cornerPx, resIds);
    }

    /**
     * @param isCircle 是否加载为圆形
     * @param cornerPx 在isCircle为false时：
     *                 cornerPx需要大于0表示加载为圆角图片
     *                 小于等于0表示直接加载图片centerCrop
     * @param resIds   在图片加载中和加载失败的占用资源
     *                 传入值为null，会使用默认的占用图资源和失败图资源
     *                 传入值为一个资源时，会将占用图资源和失败图资源都设置为该资源
     *                 传入值为两个资源时，第一个是占用图资源，第二个是失败图资源
     *                 传入值为超过两个资源时，只会使用前两个
     */
    public static void load(Context context, Object url, ImageView imageView, boolean isCircle, int cornerPx, int... resIds) {
        int[] ids = getResIds(resIds);
        if (isNoImage) {
            imageView.setImageResource(ids[0]);
            return;
        }
        RequestOptions options = new RequestOptions().placeholder(ids[0]).error(ids[1]);
        Glide.with(context)
                .load(dealUrl(url))
                .apply(isCircle ? options.circleCrop()
                        : (cornerPx > 0 ? options.transform(new RoundedCorners(cornerPx))
                        : options.centerCrop()))
                .into(imageView);
    }

    private static Object dealUrl(Object url) {
        if (url == null) {
            url = "";
        }
        return url;
    }

    private static int[] getResIds(int... resIds) {
        if (resIds == null || resIds.length == 0) {
            return new int[]{DEFAULT_PLACE_RES_ID, DEFAULT_ERROR_RES_ID};
        }
        return new int[]{resIds[0], resIds.length >= 2 ? resIds[1] : resIds[0]};
    }

}
