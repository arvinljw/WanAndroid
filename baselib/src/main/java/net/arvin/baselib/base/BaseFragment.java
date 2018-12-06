package net.arvin.baselib.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * Created by arvinljw on 2018/11/1 11:14
 * Function：
 * Desc：
 */
public abstract class BaseFragment extends Fragment implements Callback.OnReloadListener {
    protected View root;
    protected LoadService loadService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(getContentView(), container, false);
        loadService = LoadSir.getDefault().register(root, this);
        loadService.showSuccess();
        return loadService.getLoadLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onReload(View v) {
    }

    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);
}
