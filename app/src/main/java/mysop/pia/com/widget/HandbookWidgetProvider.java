package mysop.pia.com.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;

import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.R;


public class HandbookWidgetProvider extends AppWidgetProvider {

        public static List<MySOPs> mIngredients;

        public HandbookWidgetProvider()
        {

        }

        static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                    int[] appWidgetIds, List<MySOPs> ingredients)
        {
            mIngredients = ingredients;
            for (int appWidgetId : appWidgetIds)
            {
                Intent intent = new Intent(context, listViewsService.class);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                views.setRemoteAdapter(R.id.widget_recycler, intent);
                ComponentName component = new ComponentName(context, HandbookWidgetProvider.class);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recycler);
                appWidgetManager.updateAppWidget(component, views);
            }

        }


    @Override
        public void onEnabled(Context context)
        {

        }

        @Override
        public void onDisabled(Context context)
        {

        }


    }



