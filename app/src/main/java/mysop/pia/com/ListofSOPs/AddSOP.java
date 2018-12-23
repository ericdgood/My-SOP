package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.R;
import mysop.pia.com.RoomData.SopRoom.SOPRoomData;
import mysop.pia.com.RoomData.SopRoom.SopAppDatabase;

public class AddSOP extends AppCompatActivity{

    private static final String TAG = "test";
    @BindView(R.id.edittext_add_sop_title)
    EditText editTextAddSopTitle;
    @BindView(R.id.edittext_add_numberof_steps)
    EditText editTextAddNumberOfSteps;
    @BindView(R.id.button_add_sop_save)
    Button buttonAddSopSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sop);
        ButterKnife.bind(this);



        buttonAddSopSave.setOnClickListener(v -> {
//            THIS WILL SAVE THE SOP INFO
            String addSopTitle = editTextAddSopTitle.getText().toString();
            String addNumberOfSteps = editTextAddNumberOfSteps.getText().toString();
            String categoryName = CategoryRecyclerAdapter.categoryName;

            SOPRoomData newSOP = new SOPRoomData(categoryName, addSopTitle, addNumberOfSteps);

            sopRoomDatabase().listOfSOPs().insertSop(newSOP);

            Intent returnToListOfSOPs = new Intent(AddSOP.this, ListofSOPs.class);
            startActivity(returnToListOfSOPs);
            finish();
        });
    }

    public SopAppDatabase sopRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), SopAppDatabase.class, "sopinfo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
