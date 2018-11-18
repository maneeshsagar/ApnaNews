package com.example.maneeshsagar.apnanews.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.maneeshsagar.apnanews.R;

import java.util.List;

public class AppDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> collections;
    Intent intent;
    Context context;
    public AppDataProvider(Intent intent, Context context) {
        this.intent = intent;
        this.context = context;
    }


    @Override
    public void onCreate() {
        initData();
    }

    private void initData() {
        for(int i=1;i<=10;i++)
        {
            collections.add("TextView "+i);
        }
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        return null;
    }

    @Override
    public RemoteViews getLoadingView() {

        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
