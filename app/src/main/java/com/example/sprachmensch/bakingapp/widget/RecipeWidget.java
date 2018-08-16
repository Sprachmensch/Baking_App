package com.example.sprachmensch.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.sprachmensch.bakingapp.R;

public class RecipeWidget extends AppWidgetProvider {

    static String widgetIngredients = "Please select a Recipe from within the App, to do that simply click\n \'Add Recipe to Widget\'";
    static String widgetText = "Recipe";
    static String widgetQuantity = "";
    static int appWidgetId;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d("updateAppWidget", "widgetText: " + widgetText);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.ingredients_tv, widgetIngredients);
        views.setTextViewText(R.id.quantity_tv, widgetQuantity);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("onUpdate", "...");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            this.appWidgetId = appWidgetId;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("onReceive", "intent: " + intent.getSerializableExtra("recipe"));

        if (intent.getStringExtra("recipe") != null) {
            widgetQuantity = intent.getStringExtra("quantity");
            widgetIngredients = intent.getStringExtra("recipe");
            widgetText = intent.getStringExtra("recipeName");
        }

        ComponentName appWidget = new ComponentName(context, RecipeWidget.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(appWidget, views);

        onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(appWidget));
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

