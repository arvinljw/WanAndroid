package net.arvin.wanandroid.utils;

import android.content.Context;
import android.graphics.Color;

import com.bumptech.glide.Glide;

import net.arvin.baselib.utils.FileUtil;
import net.arvin.wanandroid.BuildConfig;
import net.arvin.wanandroid.uis.activities.SettingActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by arvinljw on 2018/11/23 16:02
 * Function：
 * Desc：
 */
public class Util {
    private static Integer[] colors = {Color.parseColor("#757575"), Color.parseColor("#242524"), Color.parseColor("#49617e"),
            Color.parseColor("#965e75"), Color.parseColor("#3b9a58"), Color.parseColor("#05596e"),
            Color.parseColor("#943e4f"), Color.parseColor("#0a5d17")};


    public static int getRandomColor(Random random) {
        return colors[random.nextInt(colors.length)];
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }
}
