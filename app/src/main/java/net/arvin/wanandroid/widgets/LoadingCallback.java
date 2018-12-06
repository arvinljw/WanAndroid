package net.arvin.wanandroid.widgets;

import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;

import net.arvin.wanandroid.R;


/**
 * Created by arvinljw on 2018/11/27 16:03
 * Function：
 * Desc：
 */
public class LoadingCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }
}
