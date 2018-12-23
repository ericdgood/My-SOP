package mysop.pia.com.Steps;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.ListofSOPs;
import mysop.pia.com.R;
import mysop.pia.com.RoomData.SopRoom.SOPRoomData;
import mysop.pia.com.RoomData.SopRoom.SopAppDatabase;

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

    int stepNumber = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_step);
        ButterKnife.bind(this);
//      GETS SOPTITLE FROM ADD SOP INTENT
        String sopTitle = getIntent().getStringExtra("sopTitle");

        String stepConcat = "Step" + stepNumber;
        textviewStepCount.setText(stepConcat);

        completeSOP(sopTitle, stepNumber);
    }

    private void completeSOP(String sopTitle, int stepNumber){
        buttonCompleteSOP.setOnClickListener(v -> {
         String stepTitle = ediitTextStepTitle.getText().toString();

            SOPRoomData newSOP = new SOPRoomData(categoryName, sopTitle, stepTitle, stepNumber);
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
}
