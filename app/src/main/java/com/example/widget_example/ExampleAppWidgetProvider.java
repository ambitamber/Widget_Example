package com.example.widget_example;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.widget_example.model.Articles;
import com.example.widget_example.model.EventBusResults;
import com.example.widget_example.utils.MyApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;


public class ExampleAppWidgetProvider extends AppWidgetProvider {

    ArrayList<Articles> articlesArrayList;
    public static final String ARTICLELIST = "articles";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        EventBus.getDefault().register(this);
        Log.d("WidgetProvider", "onUpdate called...");
        for (int appWidgetId : appWidgetIds){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            Intent intent = new Intent(context,ExampleWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            if (articlesArrayList != null && articlesArrayList.size() > 0 ){
                Bundle extrasBundle = new Bundle();
                extrasBundle.putParcelableArrayList(ARTICLELIST,articlesArrayList);
                intent.putExtra("bundle", extrasBundle);
            }

            remoteViews.setEmptyView(R.id.example_widget_stack_view,R.id.example_widget_empty_view);
            remoteViews.setRemoteAdapter(R.id.example_widget_stack_view,intent);
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }
        Log.d("WidgetProvider", "calling notifyAppWidgetViewDataChanged...");
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.example_widget_stack_view); // this triggers onDatasetChanged from RemoteViewsService.RemoteViewsFactory
        doThis(new EventBusResults(1,articlesArrayList));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doThis(EventBusResults eventBusResults) {
        // retrieve the data sent from Widget Service
        articlesArrayList = eventBusResults.mArticlesArrayList;

        // manually refresh widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MyApplication.getAppContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(MyApplication.getAppContext(), ExampleAppWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.example_widget_stack_view);
    }
}
