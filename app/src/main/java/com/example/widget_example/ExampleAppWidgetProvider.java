package com.example.widget_example;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;


public class ExampleAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i=0;i<appWidgetIds.length;i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.example_widget);
            Intent intent = new Intent(context, ExampleWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setEmptyView(R.id.example_widget_stack_view, R.id.example_widget_empty_view);
            rv.setRemoteAdapter(R.id.example_widget_stack_view, intent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
