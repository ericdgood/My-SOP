package mysop.pia.com.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.List;

import mysop.pia.com.Steps.ListOfSteps;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class WidgetUpdateService extends IntentService {
    private final List<StepsRoomData> mIngredients = ListOfSteps.listOfSteps;

    public WidgetUpdateService() {
        super("WidgetServiceUpdate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, HandbookWidgetProvider.class));
        HandbookWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, mIngredients);
    }
}
