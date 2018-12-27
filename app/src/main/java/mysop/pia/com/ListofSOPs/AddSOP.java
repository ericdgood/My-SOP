package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.ListofSOPs.SopRoom.SOPRoomData;
import mysop.pia.com.ListofSOPs.SopRoom.SopAppDatabase;
import mysop.pia.com.R;
import mysop.pia.com.Steps.AddStep;

import static mysop.pia.com.Categories.CategoryRecyclerAdapter.categoryName;

public class AddSOP extends AppCompatActivity{

    private static final String TAG = "test";
    @BindView(R.id.edittext_add_sop_title)
    EditText editTextAddSopTitle;
    @BindView(R.id.button_add_sop_add_step)
    Button buttonAddStep;
    String SOPTitlesCheck;
    String addSopTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sop);
        ButterKnife.bind(this);



        buttonAddStep.setOnClickListener(v -> {
//          SAVE SOP INFO
            addSopTitle = editTextAddSopTitle.getText().toString();

            if (checkDuplicateSOP()) {
                SOPRoomData newSOP = new SOPRoomData(categoryName, addSopTitle);
                sopRoomDatabase().listOfSOPs().insertSop(newSOP);

                Intent addStep = new Intent(AddSOP.this, AddStep.class);
                addStep.putExtra("sopTitle", addSopTitle);
                startActivity(addStep);
                finish();
            }
        });
    }

    public SopAppDatabase sopRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), SopAppDatabase.class, "sopinfo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private boolean checkDuplicateSOP(){
        List<SOPRoomData> SOPs = sopRoomDatabase().listOfSOPs().getAllSOPs(categoryName);

        for (int i = 0; i < SOPs.size(); i++) {
            SOPTitlesCheck = String.valueOf(SOPs.get(i).getSopTitle().toUpperCase());

            if (SOPTitlesCheck.equals(addSopTitle.toUpperCase())) {
                Toast.makeText(this, "This SOP already exists", Toast.LENGTH_LONG).show();
                return false;
            } else if (addSopTitle.equals("")){
                Toast.makeText(this, "Please enter a SOP name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
