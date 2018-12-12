package net.arvin.baselib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by arvinljw on 2018/11/1 11:15
 * Function：
 * Desc：
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(getContentView());
        init(savedInstanceState);
    }

    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);
}
