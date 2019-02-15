package mysop.pia.com.Pages;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofHandbooks.ListofHandbooksAdapter;
import mysop.pia.com.MainActivity;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;
import mysop.pia.com.widget.WidgetUpdateService;

public class ListOfPages extends Activity {

    @BindView(R.id.textview_list_steps_title)
    TextView textviewTitle;
    @BindView(R.id.recyclerview_list_of_steps)
    RecyclerView recyclerviewListOfSteps;
    @BindView(R.id.fab_list_steps_add_step)
    FloatingActionButton fabAddStep;
    @BindView(R.id.imageview_book_pages_option)
    ImageView imgBookPagesOption;

    public static List<StepsRoomData> listOfSteps = new ArrayList<>();
    ListOfPagesAdapter StepsRecyclerAdapter;
    String bookTitle;
    int sharedBook = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_steps);
        ButterKnife.bind(this);

        bookTitle = ListofHandbooksAdapter.bookTitle;

        pageListOption();
        getBookPages();

        textviewTitle.setText(bookTitle);
        setupRecyclerviewAndAdapter();
        fabAddStep();

//        DRAG AND DROP ITEMS TO REARRANGE
        rearrangeItems();

    }

    private void pageListOption() {
        imgBookPagesOption.setOnClickListener(v -> {
//            OPEN OPTIONS
            PopupMenu popup = new PopupMenu(this, imgBookPagesOption);
            popup.inflate(R.menu.pages_list_menu);
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.book_pages_widget:
                        startWidgetService();
                        return true;
                    default:
                        return false;
                }

            });
            //displaying the popup
            popup.show();
        });
    }

    private void setupRecyclerviewAndAdapter() {
        StepsRecyclerAdapter = new ListOfPagesAdapter(this, listOfSteps);
        recyclerviewListOfSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListOfSteps.setAdapter(StepsRecyclerAdapter);
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void fabAddStep() {
        fabAddStep.setOnClickListener(v -> {
            int listOfStepsSize = listOfSteps.size();
            Intent addStep = new Intent(this, AddPage.class);
            addStep.putExtra(getString(R.string.booktitle), bookTitle);
            addStep.putExtra(getString(R.string.bookcolor),ListofHandbooksAdapter.bookColor);
            addStep.putExtra(getString(R.string.pagenum), listOfStepsSize + 1);
            startActivity(addStep);
            finish();
        });
    }

    public void getBookPages() {
        List<StepsRoomData> fbsteps = MainActivity.firebaseSteps;
        listOfSteps.clear();
        for (int i = 0; i < fbsteps.size(); i++) {
            if (fbsteps.get(i).getSopTitle().equals(bookTitle) && fbsteps.get(i).getSharedStatus() == 2) {
                listOfSteps.add(fbsteps.get(i));
                sharedBook = sharedBook + 1;
            }
        }
        if (sharedBook == 0) {
            listOfSteps = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle);
        }
    }

    private void startWidgetService() {
        Intent i = new Intent(this, WidgetUpdateService.class);
        startService(i);
        Toast.makeText(this, R.string.widgetpage, Toast.LENGTH_LONG).show();
    }

    public void rearrangeItems(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                StepsRoomData selectedItem = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle).get(position_dragged);
                StepsRoomData targetItem = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle).get(position_target);
//
                stepsRoomDatabase().listOfSteps().updateSelectedItem(targetItem.getStepNumber(), selectedItem.getId());
                stepsRoomDatabase().listOfSteps().updateSelectedItem(selectedItem.getStepNumber(), targetItem.getId());
//
                StepsRecyclerAdapter.notifyItemMoved(position_dragged, position_target);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        }).attachToRecyclerView(recyclerviewListOfSteps);
    }

}
