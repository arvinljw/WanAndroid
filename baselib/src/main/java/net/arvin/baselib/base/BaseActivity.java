package net.arvin.baselib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * Created by arvinljw on 2018/11/1 11:15
 * Function：
 * Desc：
 */
public abstract class BaseActivity extends AppCompatActivity implements Callback.OnReloadListener {

    protected LoadService loadService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        init(savedInstanceState);
        loadService = LoadSir.getDefault().register(this, this);
        loadService.showSuccess();
    }

    @Override
    public void onReload(View v) {
    }

    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);
}
