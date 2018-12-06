package net.arvin.wanandroid.nets.repositories;

import android.arch.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;

import net.arvin.wanandroid.entities.ArticleEntity;
import net.arvin.wanandroid.entities.BannerEntity;
import net.arvin.wanandroid.entities.HotKeyEntity;
import net.arvin.wanandroid.entities.NavigationInfoEntity;
import net.arvin.wanandroid.entities.PageList;
import net.arvin.wanandroid.entities.Response;
import net.arvin.wanandroid.entities.TreeEntity;
import net.arvin.wanandroid.nets.Net;

import java.util.List;

/**
 * Created by arvinljw on 2018/11/16 16:25
 * Function：
 * Desc：
 */
public class ArticlesRepo {
    public static LiveData<Resource<Response<List<BannerEntity>>>> getBanners() {
        return Net.api().getBanners();
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> getIndexArticles(int page) {
        return Net.api().getIndexArticles(page);
    }

    public static LiveData<Resource<Response<List<HotKeyEntity>>>> getHotKeys() {
        return Net.api().getHotKeys();
    }

    public static LiveData<Resource<Response<List<TreeEntity>>>> getKnowledgeTree() {
        return Net.api().getKnowledgeTree();
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> getKnowledgeTreeArticles(int page, int cid) {
        return Net.api().getKnowledgeTreeArticles(page, cid);
    }

    public static LiveData<Resource<Response<List<NavigationInfoEntity>>>> getNavigationInfo() {
        return Net.api().getNavigationInfo();
    }

    public static LiveData<Resource<Response<List<TreeEntity>>>> getProjects() {
        return Net.api().getProjects();
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> getProjectArticles(int page, int cid) {
        return Net.api().getProjectArticles(page, cid);
    }

    public static LiveData<Resource<Response<PageList<ArticleEntity>>>> getSearchArticles(int currPage, String searchKey) {
        return Net.api().getSearchArticles(currPage, searchKey);
    }
}
