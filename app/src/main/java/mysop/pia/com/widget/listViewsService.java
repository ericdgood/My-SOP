package mysop.pia.com.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import mysop.pia.com.Pages.ListOfPages;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;


public class listViewsService extends RemoteViewsService
{

    public ListViewsFactory onGetViewFactory(Intent intent)
    {
        return new ListViewsFactory(this.getApplicationContext());
    }
}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private final Context mContext;
    private List<StepsRoomData> mIngredients = ListOfPages.listOfSteps;

    ListViewsFactory(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public void onCreate()
    {
    }

    //Very Important,this is the place where the data is being changed each time by the adapter.
    @Override
    public void onDataSetChanged()
    {
        mIngredients = HandbookWidgetProvider.mIngredients;
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public int getCount()
    {
        if (mIngredients == null)
            return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    { String number = String.valueOf(mIngredients.get(position).getStepNumber()) + "..";
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.widget_page_number, number);
        views.setTextViewText(R.id.widget_page_title, mIngredients.get(position).getStepTitle());
        return views;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
