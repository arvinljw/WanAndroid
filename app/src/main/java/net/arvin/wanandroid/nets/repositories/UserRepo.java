package net.arvin.wanandroid.nets.repositories;

import android.arch.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;

import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.LoginResultEntity;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.nets.Net;

import retrofit2.http.Field;
import retrofit2.http.Path;

/**
 * Created by arvinljw on 2018/11/30 10:45
 * Function：
 * Desc：
 */
public class UserRepo {
    public static LiveData<Resource<Response<LoginResultEntity>>> login(String username, String password) {
        return Net.api().login(username, password);
    }

    public static LiveData<Resource<Response<LoginResultEntity>>> register(String username, String password, String repassword) {
        return Net.api().register(username, password, repassword);
    }

    public static LiveData<Resource<Response<LoginResultEntity>>> logout() {
        return Net.api().logout();
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> getCollectionArticles(int page) {
        return Net.api().getCollectionArticles(page);
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> collectArticle(int id) {
        return Net.api().collectArticle(id);
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> collectOriginArticle(String title, String author, String link) {
        return Net.api().collectOriginArticle(title, author, link);
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> cancelCollectOriginArticle(int id) {
        return Net.api().cancelCollectOriginArticle(id, -1);
    }
}
