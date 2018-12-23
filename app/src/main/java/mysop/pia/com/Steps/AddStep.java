package mysop.pia.com.Steps;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.ListofSOPs;
import mysop.pia.com.R;
import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.ListofSOPs.SopRoom.SopAppDatabase;
import mysop.pia.com.Steps.SopRoom.StepsAppDatabase;
import mysop.pia.com.Steps.SopRoom.StepsRoomData;

import static mysop.pia.com.Categories.CategoryRecyclerAdapter.categoryName;

public class AddStep extends AppCompatActivity {

    @BindView(R.id.textview_add_sop_step_count)
    TextView textviewStepCount;
    @BindView(R.id.edittext_add_step_title)
    EditText ediitTextStepTitle;
    @BindView(R.id.button_add_sop_another_step)
    Button buttonAddAnotherStep;
    @BindView(R.id.button_add_sop_save)
    Button buttonCompleteSOP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_step);
        ButterKnife.bind(this);
//      GETS SOPTITLE FROM ADD SOP INTENT
        String sopTitle = getIntent().getStringExtra("sopTitle");
        int stepNumber = getIntent().getIntExtra("stepNumber", 1);

        String stepConcat = "Step" + stepNumber;
        textviewStepCount.setText(stepConcat);

        completeSOP(sopTitle, stepNumber);
        addStepToRoom(stepNumber);
    }

    private void completeSOP(String sopTitle, int stepNumber){
        buttonCompleteSOP.setOnClickListener(v -> {
//          SAVE SOP INFO
            SOPRoomData newSOP = new SOPRoomData(categoryName, sopTitle, stepNumber);
            sopRoomDatabase().listOfSOPs().insertSop(newSOP);

            Intent returnToListOfSOPs = new Intent(this, ListofSOPs.class);
            startActivity(returnToListOfSOPs);
        });
    }

    public SopAppDatabase sopRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), SopAppDatabase.class, "sopinfo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void addStepToRoom(int stepNumber){
        buttonAddAnotherStep.setOnClickListener((View v) -> {
            String stepTitle = ediitTextStepTitle.getText().toString();
//            SAVE STEPS FOR SOP
            StepsRoomData newStep = new StepsRoomData(stepTitle, stepNumber);
            stepsRoomDatabase().listOfSteps().insertSteps(newStep);
            Intent nextStep = new Intent(this, AddStep.class);
            int nextStepNum = stepNumber + 1;
            nextStep.putExtra("stepNumber", nextStepNum);
            startActivity(nextStep);
        });
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
