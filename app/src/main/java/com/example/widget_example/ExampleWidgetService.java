package com.example.widget_example;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.widget_example.model.Articles;
import com.example.widget_example.model.EventBusResults;
import com.example.widget_example.model.News;
import com.example.widget_example.utils.ApiClient;
import com.example.widget_example.utils.ApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExampleWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ExampleWidgetItemFactory(getApplicationContext(), intent);
    }


    private static class ExampleWidgetItemFactory implements RemoteViewsFactory {

        private Context context;
        private News news;
        private ArrayList<Articles> articlesArrayList = new ArrayList<>();

        public ExampleWidgetItemFactory(Context applicationContext, Intent intent) {

            this.context = applicationContext;
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                articlesArrayList = bundle.getParcelableArrayList(ExampleAppWidgetProvider.ARTICLELIST);
            }
        }
        private void getData(){
            ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
            Call<News> call = apiService.getheadlines("us","");
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    news = response.body();
                    articlesArrayList = news.getArticles();
                    Log.i("ExampleWidgetService",  news.getStatus());
                    EventBus.getDefault().post(new EventBusResults(1,articlesArrayList));
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Log.i("ExampleWidgetService",t.getMessage());
                }
            });
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            // if the data is already available, then don't kick off network request
            if (articlesArrayList != null && articlesArrayList.size() > 0) {
                return;
            }
            getData();
        }

        @Override
        public void onDestroy() {
            articlesArrayList = null;

        }

        @Override
        public int getCount() {
            if (articlesArrayList == null){
               return 0;
            }
            return articlesArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.example_widget_item);
            remoteViews.setTextViewText(R.id.example_widget_item_text,articlesArrayList.get(position).getTitle());
            Log.i("RemoteViews",  " is called.");
            return remoteViews;
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
            return false;
        }
    }
}
