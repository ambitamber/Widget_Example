package com.example.widget_example;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.widget_example.model.Articles;
import com.example.widget_example.model.News;
import com.example.widget_example.utils.ApiClient;
import com.example.widget_example.utils.ApiService;

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


    private class ExampleWidgetItemFactory implements RemoteViewsFactory {

        private Context context;
        private int appWidgetId;
        private News news;
        private ArrayList<Articles> articlesArrayList = new ArrayList<>();

        public ExampleWidgetItemFactory(Context applicationContext, Intent intent) {

            this.context = applicationContext;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        private void getData(){
            ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
            Call<News> call = apiService.getheadlines("us","5b5ab3f933184289a68cae008cd352d1");
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    news = response.body();
                    articlesArrayList = news.getArticles();
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
        }

        @Override
        public void onCreate() {
            getData();
        }

        @Override
        public void onDataSetChanged() {
            getData();
        }

        @Override
        public void onDestroy() {
            articlesArrayList.clear();

        }

        @Override
        public int getCount() {
            return articlesArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.example_widget);
            Articles articles = articlesArrayList.get(position);
            remoteViews.setTextViewText(R.id.example_widget_item_text,articles.getTitle());
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