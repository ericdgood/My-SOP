package mysop.pia.com.Steps;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListOfSteps extends AppCompatActivity {

    @BindView(R.id.textview_list_steps_title)
    TextView textviewTitle;
    @BindView(R.id.recyclerview_list_of_steps)
    RecyclerView recyclerviewListOfSteps;
    @BindView(R.id.fab_list_steps_add_step)
    FloatingActionButton fabAddStep;

    List<StepsRoomData> listOfSteps = new ArrayList<>();
    String sopTitle;
    int lastStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_steps);
        ButterKnife.bind(this);
        sopTitle = getIntent().getStringExtra("sopTitle");

        textviewTitle.setText(sopTitle);
        setupRecyclerviewAndAdapter(sopTitle);
        fabAddStep();
    }

    private void setupRecyclerviewAndAdapter(String sopTitle) {
        listOfSteps = stepsRoomDatabase().listOfSteps().getAllSteps(sopTitle);
        ListOfStepsAdapter StepsRecyclerAdapter = new ListOfStepsAdapter(this, listOfSteps);
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
            addStep.putExtra("sopTitle", sopTitle);
            addStep.putExtra("stepNumber", listOfStepsSize + 1);
            startActivity(addStep);
            finish();
        });
    }
}
