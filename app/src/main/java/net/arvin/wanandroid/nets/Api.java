package net.arvin.wanandroid.nets;

import android.arch.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;

import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.BannerEntity;
import net.arvin.wanandroid.entities.HotKeyEntity;
import net.arvin.wanandroid.entities.LoginResultEntity;
import net.arvin.wanandroid.entities.NavigationInfoEntity;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.PublicNumberEntity;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.entities.TreeEntity;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by arvinljw on 2018/10/31 14:43
 * Function：
 * Desc：
 */
public interface Api {
    String BASE_URL = "http://wanandroid.com/";

    /*=======首页======*/
    @GET("article/list/{page}/json")
    LiveData<Resource<Response<PageList<ArticleEntity>>>> getIndexArticles(@Path("page") int page);

    @GET("banner/json")
    LiveData<Resource<Response<List<BannerEntity>>>> getBanners();

    @GET("hotkey/json")
    LiveData<Resource<Response<List<HotKeyEntity>>>> getHotKeys();

    @POST("article/query/{page}/json")
    @FormUrlEncoded
    LiveData<Resource<Response<PageList<ArticleEntity>>>> getSearchArticles(@Path("page") int page, @Field("k") String k);

    /*=======知识体系======*/

    @GET("tree/json")
    LiveData<Resource<Response<List<TreeEntity>>>> getKnowledgeTree();

    @GET("article/list/{page}/json")
    LiveData<Resource<Response<PageList<ArticleEntity>>>> getKnowledgeTreeArticles(@Path("page") int page, @Query("cid") int cid);

    /*=======导航======*/
    @GET("navi/json")
    LiveData<Resource<Response<List<NavigationInfoEntity>>>> getNavigationInfo();

    /*=======项目======*/
    @GET("project/tree/json")
    LiveData<Resource<Response<List<TreeEntity>>>> getProjects();

    @GET("project/list/{page}/json")
    LiveData<Resource<Response<PageList<ArticleEntity>>>> getProjectArticles(@Path("page") int page, @Query("cid") int cid);

    /*=======公众号======*/
    @GET("wxarticle/chapters/json")
    LiveData<Resource<Response<List<PublicNumberEntity>>>> getPublicNumber();

    /*=======登陆注册======*/
    @POST("user/login")
    @FormUrlEncoded
    LiveData<Resource<Response<LoginResultEntity>>> login(@Field("username") String username, @Field("password") String password);

    @POST("user/register")
    @FormUrlEncoded
    LiveData<Resource<Response<LoginResultEntity>>> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @GET("user/logout/json")
    LiveData<Resource<Response<LoginResultEntity>>> logout();

    /*=======收藏======*/
    @GET("lg/collect/list/{page}/json")
    LiveData<Resource<Response<PageList<ArticleEntity>>>> getCollectionArticles(@Path("page") int page);

    @POST("lg/collect/{id}/json")
    LiveData<Resource<Response<PageList<ArticleEntity>>>> collectArticle(@Path("id") int id);

    @POST("lg/collect/add/json")
    @FormUrlEncoded
    LiveData<Resource<Response<PageList<ArticleEntity>>>> collectOriginArticle(@Field("title") String title, @Field("author") String author, @Field("link") String link);

    /**
     * 取消收藏页面站内文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    @FormUrlEncoded
    LiveData<Resource<Response<PageList<ArticleEntity>>>> cancelCollectOriginArticle(@Path("id") int id, @Field("originId") int originId);

}
