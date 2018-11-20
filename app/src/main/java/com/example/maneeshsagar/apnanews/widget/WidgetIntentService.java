package com.example.maneeshsagar.apnanews.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.RemoteViews;
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
    final static String SOURCE="google-news-in";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    ArticleStructure articleStructure;
    public WidgetIntentService() {
        super("Maneesh");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       int id= R.id.update;
        ApiInterface request=ApiClient.getClient().create(ApiInterface.class);
        Call<NewsResponse> call = request.getHeadlines(SOURCE, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                    articleStructure=response.body().getArticles().get(BASE_ELEMENT);
            }


            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),"Connect to the Internet",Toast.LENGTH_LONG);
            }
        });
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(getApplicationContext());
       // int[] appid=appWidgetManager.getAppWidgetIds(new ComponentName(this,CollectionWidget.class));
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.collection_widget);
        remoteViews.setTextViewText(R.id.update,articleStructure.getTitle());
        ComponentName thisWidget = new ComponentName(getApplicationContext(), CollectionWidget.class);
      //  CollectionWidget.updateAppWidget(thisWidget,appWidgetManager);
        appWidgetManager.updateAppWidget(thisWidget,remoteViews);

    }
}
