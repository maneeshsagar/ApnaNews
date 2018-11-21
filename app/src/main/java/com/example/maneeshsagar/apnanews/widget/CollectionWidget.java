package com.example.maneeshsagar.apnanews.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.maneeshsagar.apnanews.MainActivity;
import com.example.maneeshsagar.apnanews.R;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences=context.getSharedPreferences(WidgetIntentService.MYPREF,Context.MODE_PRIVATE);
        String s=sharedPreferences.getString(WidgetIntentService.BASE,"NULL");
        String s1=sharedPreferences.getString(WidgetIntentService.FIRST_ELEMENT_STRING,"NULL");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);
        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pendingResult=PendingIntent.getActivity(context,0,intent,0);
        views.setTextViewText(R.id.update,s);
        views.setTextViewText(R.id.update2,s1);
        views.setOnClickPendingIntent(R.id.widget_layout_main,pendingResult);
        // Instruct the widget manager to update the widget
        context.startService(intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {


            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

