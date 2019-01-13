package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.Steps.AddStep;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

import static mysop.pia.com.Categories.CategoryRecyclerAdapter.categoryName;

public class AddSOP extends AppCompatActivity {

    private static final String TAG = "hello";
    @BindView(R.id.edittext_add_sop_title)
    EditText editTextAddSopTitle;
    @BindView(R.id.button_edit_sop)
    Button buttonEditSOP;
    @BindView(R.id.button_add_sop_add_step)
    Button buttonAddStep;
    @BindView(R.id.textview_new_sop_label)
    TextView tvAddBookLabel;
    @BindView(R.id.book_color_blue)
    ImageView viewBookBlue;
    @BindView(R.id.book_color_green)
    ImageView viewBookGreen;
    @BindView(R.id.book_color_red)
    ImageView viewBookRed;
    @BindView(R.id.book_color_yellow)
    ImageView viewBookYellow;
    @BindView(R.id.book_color_orange)
    ImageView viewBookOrange;

    String SOPTitlesCheck;
    String addSopTitle;
    int EDITSOP;
    String bookColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sop);
        ButterKnife.bind(this);

        EDITSOP = getIntent().getIntExtra("editSop", 0);

        if (EDITSOP == 1) {
            editSop();
        } else {
            bookColor();
            buttonAddStep.setOnClickListener(v -> {
//          SAVE SOP INFO
                addSopTitle = editTextAddSopTitle.getText().toString();

                if (checkDuplicateSOP()) {
                    Intent addStep = new Intent(AddSOP.this, AddStep.class);
                    addStep.putExtra("sopTitle", addSopTitle);
                    addStep.putExtra("sopCategory", categoryName);
                    addStep.putExtra("bookColor", bookColor);
                    startActivity(addStep);
                    finish();
                }
            });
        }
    }

    public void bookColor() {
        viewBookRed.setOnClickListener(v -> {
            bookColor = "Red";toastColor();
        });
        viewBookBlue.setOnClickListener(v -> {
            bookColor = "Blue";toastColor();
        });
        viewBookGreen.setOnClickListener(v -> {
            bookColor = "Green";toastColor();
        });
        viewBookYellow.setOnClickListener(v -> {
            bookColor = "Yellow";toastColor();
        });
        viewBookOrange.setOnClickListener(v -> {
            bookColor = "Orange"; toastColor();
        });
    }

    private void toastColor(){
        Toast.makeText(this, bookColor + " was selected", Toast.LENGTH_SHORT).show();
    }

    private void editSop() {
//        TODO CHANGE BOOK SHELF
        buttonAddStep.setVisibility(View.GONE);
        buttonEditSOP.setVisibility(View.VISIBLE);
        tvAddBookLabel.setText("Edit Book info");
        editTextAddSopTitle.setText(getIntent().getStringExtra("editSopTitle"));
        bookColor();
        buttonEditSOP.setOnClickListener(v -> {
            addSopTitle = editTextAddSopTitle.getText().toString();

                stepsRoomDatabase().listOfSteps().updateSop(addSopTitle, getIntent().getStringExtra("editSopTitle"));
                stepsRoomDatabase().listOfSteps().updateBookColor(bookColor,getIntent().getIntExtra("editId",0));
                Intent returnToSOP = new Intent(this, ListofSOPs.class);
                startActivity(returnToSOP);
                finish();
        });
    }

    private boolean checkDuplicateSOP() {
        List<StepsRoomData> SOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(categoryName);


        for (int i = 0; i < SOPs.size(); i++) {
            SOPTitlesCheck = String.valueOf(SOPs.get(i).getSopTitle().toUpperCase());

            if (SOPTitlesCheck.equals(addSopTitle.toUpperCase())) {
                String sopCategory = SOPs.get(i).getCategory();
                Toast.makeText(this, "This SOP already exists in " + sopCategory + " category", Toast.LENGTH_LONG).show();
                return false;
            } else if (addSopTitle.equals("")) {
                Toast.makeText(this, "Please enter a SOP name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
