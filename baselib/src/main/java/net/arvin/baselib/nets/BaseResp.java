package net.arvin.baselib.nets;

import com.google.gson.JsonElement;

/**
 * Created by arvinljw on 2018/11/26 18:04
 * Function：
 * Desc：
 */
public class BaseResp<T> {
    protected T data;
    private Throwable throwable;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return data != null && throwable == null;
    }

    static <T> BaseResp<T> success(T body) {
        final BaseResp<T> resource = new BaseResp<>();
        resource.data = body;
        return resource;
    }

    static <T> BaseResp<T> error(Throwable error) {
        final BaseResp<T> resource = new BaseResp<>();
        resource.throwable = error;
        return resource;
    }

}
