package mysop.pia.com.Steps;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.ListofSOPsAdapter;
import mysop.pia.com.MainActivity;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListOfSteps extends Activity {

    private static final String TAG = "List of Steps";
    @BindView(R.id.textview_list_steps_title)
    TextView textviewTitle;
    @BindView(R.id.recyclerview_list_of_steps)
    RecyclerView recyclerviewListOfSteps;
    @BindView(R.id.fab_list_steps_add_step)
    FloatingActionButton fabAddStep;

    public static List<StepsRoomData> listOfSteps = new ArrayList<>();
    ListOfStepsAdapter StepsRecyclerAdapter;
    String bookTitle;
    int sharedBook = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_steps);
        ButterKnife.bind(this);

        bookTitle =ListofSOPsAdapter.bookTitle;

        getFirebaseBooks();
        if (sharedBook == 0) {
            listOfSteps = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle);
        }

        textviewTitle.setText(bookTitle);
        setupRecyclerviewAndAdapter();
        fabAddStep();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();
//              TODO: MAKE NUMBERS SHOW SWAP
                StepsRecyclerAdapter.notifyItemMoved(position_dragged, position_target);
                StepsRecyclerAdapter.notifyItemChanged(position_dragged, 1);

                int draggedId = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle).get(position_dragged).getId();
                int draggedStepNum = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle).get(position_dragged).getStepNumber();

                int targetId = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle).get(position_target).getId();
                int targetStepNum = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle).get(position_target).getStepNumber();

                stepsRoomDatabase().listOfSteps().updateOnMove(targetStepNum, draggedId);
                stepsRoomDatabase().listOfSteps().updateTarget(draggedStepNum, targetId);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        }).attachToRecyclerView(recyclerviewListOfSteps);
    }

    private void setupRecyclerviewAndAdapter() {
        StepsRecyclerAdapter = new ListOfStepsAdapter(this, listOfSteps);
        recyclerviewListOfSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListOfSteps.setAdapter(StepsRecyclerAdapter);
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void fabAddStep(){
        fabAddStep.setOnClickListener(v -> {
            int listOfStepsSize = listOfSteps.size();
            Intent addStep = new Intent(this, AddStep.class);
            addStep.putExtra("sopTitle", bookTitle);
            addStep.putExtra("stepNumber", listOfStepsSize + 1);
            startActivity(addStep);
            finish();
        });
    }

    private void getFirebaseBooks(){
        List<StepsRoomData> fbsteps = MainActivity.firebaseSteps;
        listOfSteps.clear();
        for (int i = 0; i < fbsteps.size(); i++) {
            if (fbsteps.get(i).getSopTitle().equals(bookTitle)) {
                listOfSteps.add(fbsteps.get(i));
                sharedBook = sharedBook + 1;
            }
        }
    }

}
