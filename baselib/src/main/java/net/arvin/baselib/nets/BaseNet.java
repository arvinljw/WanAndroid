package net.arvin.baselib.nets;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory;
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory;

import net.arvin.baselib.nets.interceptors.AppendHeaderParamInterceptor;
import net.arvin.baselib.utils.OkHttpsUtil;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by arvinljw on 2018/10/31 10:56
 * Function：
 * Desc：
 */
public abstract class BaseNet<T> {
    private T api;
    private Class<T> apiClazz;

    private OkHttpClient httpClient;
    private CallAdapter.Factory callAdapterFactory;
    private Converter.Factory converterFactory;
    private AppendHeaderParamInterceptor headerParamInterceptor;

    @SuppressWarnings("unchecked")
    protected BaseNet() {
        //默认的api的class对象，因为用到了泛型所以这个值肯定是会随着T的改变而改变，所以没必要重写
        apiClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 默认使用Gson转换器，可以根据后台情况约定返回类型，并自定义转换器
     */
    protected Converter.Factory getDefaultConvertFactory() {
        if (converterFactory == null) {
            converterFactory = GsonConverterFactory.create();
        }
        return converterFactory;
    }

    /**
     * 默认的数据返回处理adapter
     */
    protected CallAdapter.Factory getDefaultCallAdapterFactory() {
        if (callAdapterFactory == null) {
            callAdapterFactory = LiveDataCallAdapterFactory.create();
        }
        return callAdapterFactory;
    }

    @SuppressWarnings("ConstantConditions")
    protected OkHttpClient getHttpClient() {
        if (httpClient == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(getDefaultInterceptor());
            addHttpConfig(clientBuilder);
            if (isHttpsRequest()) {
                buildHttps(clientBuilder);
            }
            httpClient = clientBuilder.proxy(Proxy.NO_PROXY).build();
            makeGlideSupportHttps();
        }
        return httpClient;
    }

    protected void addHttpConfig(OkHttpClient.Builder clientBuilder) {
    }

    /**
     * @return 返回默认请求拦截器
     */
    protected Interceptor getDefaultInterceptor() {
        if (headerParamInterceptor == null) {
            headerParamInterceptor = new AppendHeaderParamInterceptor();
        }
        return headerParamInterceptor;
    }

    /**
     * 若是https请求，则还需要重写{@link #getCertificateNames()}和{@link #getApplicationContext()}方法
     */
    protected boolean isHttpsRequest() {
        return false;
    }

    protected void buildHttps(OkHttpClient.Builder clientBuilder) {
        SSLSocketFactory sslSocketFactory = OkHttpsUtil.getSslSocketFactory(getApplicationContext(), getCertificateNames());
        if (sslSocketFactory != null) {
            clientBuilder.sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
    }

    /**
     * https请求证书名字，需要存放在assets文件夹中
     *
     * @return 返回证书的名字，例如：文件名.后缀名
     */
    protected String[] getCertificateNames() {
        return null;
    }

    /**
     * 用于https请求是加载证书所用
     *
     * @return 返回ApplicationContext即可
     */
    protected Context getApplicationContext() {
        return null;
    }

    /**
     * 为glide添加https支持
     */
    private void makeGlideSupportHttps() {
        if (!isHttpsRequest()) {
            return;
        }
        Glide.get(getApplicationContext()).getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(httpClient));
    }

    public T getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getHttpClient())
                    .baseUrl(getDefaultBaseUrl())
                    .addCallAdapterFactory(getDefaultCallAdapterFactory())
                    .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
                    .addConverterFactory(getDefaultConvertFactory())
                    .build();
            api = retrofit.create(apiClazz);
        }
        return api;
    }

    public void addHeader(String key, String value) {
        headerParamInterceptor.addHeader(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        headerParamInterceptor.addHeaders(headers);
    }

    /**
     * @return 返回baseUrl，最好保证统一，注意一定要'/'符号结尾
     */
    protected abstract String getDefaultBaseUrl();

}
