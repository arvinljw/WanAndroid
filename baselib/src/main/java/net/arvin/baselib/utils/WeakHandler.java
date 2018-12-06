package net.arvin.baselib.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by arvinljw on 18/10/31 16:31
 * Function：避免内存泄露的Handler
 * Desc：使用弱引用
 */
public class WeakHandler extends Handler {

    private WeakReference<IHandle> mHandler;

    public WeakHandler(IHandle handler) {
        mHandler = new WeakReference<>(handler);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mHandler != null) {
            IHandle weakHandleInterface = mHandler.get();
            if (weakHandleInterface != null) {
                weakHandleInterface.handleMessage(msg);
            }
        }
    }

    public interface IHandle {
        void handleMessage(Message msg);
    }
}
