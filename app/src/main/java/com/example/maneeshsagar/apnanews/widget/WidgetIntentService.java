package com.example.maneeshsagar.apnanews.widget;

import android.app.IntentService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.widget.Toast;

import com.example.maneeshsagar.apnanews.ApiClient;
import com.example.maneeshsagar.apnanews.ApiInterface;
import com.example.maneeshsagar.apnanews.Constants;
import com.example.maneeshsagar.apnanews.MainActivity;
import com.example.maneeshsagar.apnanews.R;
import com.example.maneeshsagar.apnanews.adapter.DataAdapter;
import com.example.maneeshsagar.apnanews.model.ArticleStructure;
import com.example.maneeshsagar.apnanews.model.MainViewModel;
import com.example.maneeshsagar.apnanews.model.NewsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WidgetIntentService extends IntentService {

    final static int BASE_ELEMENT=0;
    public static final String MYPREF="mypref";
    final static String SOURCE="google-news-in";
    public static final String BASE="element";
    public static final String FIRST_ELEMENT_STRING="first";
    public static final int FIRST_ELEMENT=1;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    ArticleStructure articleStructure;
    ArticleStructure articleStructure1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public WidgetIntentService() {
        super("Maneesh");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sharedPreferences=getSharedPreferences(MYPREF,Context.MODE_PRIVATE);
       int id= R.id.update;
        final ApiInterface request=ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = request.getHeadlines(SOURCE, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                    articleStructure=response.body().getArticles().get(BASE_ELEMENT);
                    articleStructure1=response.body().getArticles().get(FIRST_ELEMENT);
                    editor.putString(BASE,articleStructure.getTitle());
                    editor.putString(FIRST_ELEMENT_STRING,articleStructure1.getTitle());
                    editor.commit();
            }


            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Connect to the Internet",Toast.LENGTH_LONG);
            }
        });



    }
}
