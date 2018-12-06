package net.arvin.baselib.nets.interceptors;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by arvinljw on 2018/10/31 14:18
 * Function：
 * Desc：
 */
public class AppendUrlParamInterceptor implements Interceptor {
    private Map<String, String> urlParameters;

    public AppendUrlParamInterceptor() {
        urlParameters = new HashMap<>();
    }

    public void addUrlParameter(String key, String value) {
        urlParameters.put(key, value);
    }

    public void addUrlParameters(Map<String, String> urlParameters) {
        this.urlParameters.putAll(urlParameters);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl.Builder urlBuilder = oldRequest.url().newBuilder();

        for (String key : urlParameters.keySet()) {
            urlBuilder.addQueryParameter(key, urlParameters.get(key));
        }

        Request newRequest = oldRequest
                .newBuilder()
                .url(urlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}
