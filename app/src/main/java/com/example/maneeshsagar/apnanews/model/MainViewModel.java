package com.example.maneeshsagar.apnanews.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.graphics.Movie;
import android.support.annotation.NonNull;

import com.example.maneeshsagar.apnanews.Database.AppDatabase;

import java.util.List;

/**
 * Created by maneeshsagar on 30-07-2018.
 */

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<ArticleStructure>> list;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mDb=AppDatabase.getsInstance(this.getApplication());
        list=mDb.queryDao().loadAllArticle();
    }

    public LiveData<List<ArticleStructure>> getList() {
        return list;
    }
}
