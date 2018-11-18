package com.example.maneeshsagar.apnanews.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.maneeshsagar.apnanews.model.ArticleStructure;


import java.util.List;

/**
 * Created by maneeshsagar on 27-07-2018.
 */
@Dao
public interface QueryDao {
    @Query("SELECT * FROM article")
    LiveData<List<ArticleStructure>> loadAllArticle();

    @Query("SELECT * FROM article WHERE url LIKE:url")
    ArticleStructure getMovieID(String url);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(ArticleStructure aMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(ArticleStructure aMovie);

    @Delete
    void deleteMovie(ArticleStructure aMovie);

}
