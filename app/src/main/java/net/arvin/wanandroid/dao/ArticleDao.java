package net.arvin.wanandroid.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.arvin.wanandroid.entities.ArticleEntity;

import java.util.List;

/**
 * Created by arvinljw on 2018/12/17 11:04
 * Function：
 * Desc：
 */
@Dao
public interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ArticleEntity> articles);

    @Query("select * from article order by publishTime desc")
    List<ArticleEntity> getArticles();
}
