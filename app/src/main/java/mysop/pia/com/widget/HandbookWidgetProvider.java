package mysop.pia.com.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import java.util.List;

import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;


public class HandbookWidgetProvider extends AppWidgetProvider {

    public static List<StepsRoomData> mIngredients;

    public HandbookWidgetProvider() {

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, List<StepsRoomData> ingredients) {
        mIngredients = ingredients;
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, listViewsService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setViewVisibility(R.id.widget_no_book_selected, View.GONE);
//                GET BOOK TITLE TO SHOW IN WIDGET
            String widgetTitle = mIngredients.get(0).getSopTitle() + " Handbook";
            views.setTextViewText(R.id.widget_book_title, widgetTitle);

            views.setRemoteAdapter(R.id.widget_recycler, intent);
            ComponentName component = new ComponentName(context, HandbookWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recycler);
            appWidgetManager.updateAppWidget(component, views);
        }

    }


    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }


}



