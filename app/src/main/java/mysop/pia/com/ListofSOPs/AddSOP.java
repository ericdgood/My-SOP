package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.RoomData.SOPRoomData;
import mysop.pia.com.RoomData.SopAppDatabase;

public class AddSOP extends AppCompatActivity{

    private static final String TAG = "tester";
    @BindView(R.id.edittext_add_sop_title)
    EditText editTextAddSopTitle;
    @BindView(R.id.edittext_add_numberof_steps)
    EditText editTextAddNumberOfSteps;
    @BindView(R.id.button_add_sop_save)
    Button buttonAddSopSave;

    ListofSOPs sopDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sop);
        ButterKnife.bind(this);

        final SopAppDatabase db = Room.databaseBuilder(getApplicationContext(), SopAppDatabase.class, "sopinfo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        buttonAddSopSave.setOnClickListener(v -> {
//            THIS WILL SAVE THE SOP INFO
            String addSopTitle = editTextAddSopTitle.getText().toString();
            String addNumberOfSteps = editTextAddNumberOfSteps.getText().toString();

            SOPRoomData newSOP = new SOPRoomData(addSopTitle, addNumberOfSteps);
            db.listOfSOPs().insertSop(newSOP);

            Intent returnToListOfSOPs = new Intent(AddSOP.this, ListofSOPs.class);
            startActivity(returnToListOfSOPs);
            finish();

        });

    }
}
