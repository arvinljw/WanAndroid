package net.arvin.wanandroid.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import net.arvin.wanandroid.entities.ArticleEntity;

/**
 * Created by arvinljw on 2018/12/17 11:02
 * Function：
 * Desc：
 */
@Database(entities = {ArticleEntity.class}, version = 1, exportSchema = false)
public abstract class ArticleDatabase extends RoomDatabase {
    abstract ArticleDao getArticleDao();
}
