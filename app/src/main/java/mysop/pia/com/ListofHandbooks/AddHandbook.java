package mysop.pia.com.ListofHandbooks;

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
import mysop.pia.com.Pages.AddPage;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;

import static mysop.pia.com.Categories.ShelfRecyclerAdapter.categoryName;

public class AddHandbook extends AppCompatActivity {

    private static final String TAG = "testing";
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

        EDITSOP = getIntent().getIntExtra(getString(R.string.editbook), 0);

        if (EDITSOP == 1) {
            editSop();
        } else {
            bookColor();
                buttonAddStep.setOnClickListener(v -> {
                    if (bookColor != null) {
//          SAVE SOP INFO
                        addSopTitle = editTextAddSopTitle.getText().toString();

                        if (checkDuplicateSOP()) {
                            Intent addStep = new Intent(AddHandbook.this, AddPage.class);
                            addStep.putExtra(getString(R.string.booktitle), addSopTitle);
                            addStep.putExtra(getString(R.string.bookshelf), categoryName);
                            addStep.putExtra(getString(R.string.bookcolor), bookColor);
                            startActivity(addStep);
                            finish();
                        }
                    }else {
                        Toast.makeText(this, "Please select a book color", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    private void clearColorBorder(){
        viewBookRed.setImageResource(0);
        viewBookBlue.setImageResource(0);
        viewBookGreen.setImageResource(0);
        viewBookYellow.setImageResource(0);
        viewBookOrange.setImageResource(0);
    }

    public void bookColor() {
        viewBookRed.setOnClickListener(v -> {
            bookColor = getString(R.string.red);
            clearColorBorder();
            viewBookRed.setImageResource(R.drawable.description_border);
            toastColor();
        });
        viewBookBlue.setOnClickListener(v -> {
            bookColor = getString(R.string.blue);
            clearColorBorder();
            viewBookBlue.setImageResource(R.drawable.description_border);
            toastColor();
        });
        viewBookGreen.setOnClickListener(v -> {
            bookColor = getString(R.string.green);
            clearColorBorder();
            viewBookGreen.setImageResource(R.drawable.description_border);
            toastColor();
        });
        viewBookYellow.setOnClickListener(v -> {
            bookColor = getString(R.string.yellow);
            clearColorBorder();
            viewBookYellow.setImageResource(R.drawable.description_border);
            toastColor();
        });
        viewBookOrange.setOnClickListener(v -> {
            bookColor = getString(R.string.orange);
            clearColorBorder();
            viewBookOrange.setImageResource(R.drawable.description_border);
            toastColor();
        });
    }

    private void toastColor(){
        Toast.makeText(this, bookColor + getString(R.string.wasselected), Toast.LENGTH_SHORT).show();
    }

    private void editSop() {
        buttonAddStep.setVisibility(View.GONE);
        buttonEditSOP.setVisibility(View.VISIBLE);
        tvAddBookLabel.setText(R.string.book_info);
        editTextAddSopTitle.setText(getIntent().getStringExtra(getString(R.string.editbooktitle)));
        bookColor();
        showBookColor(getIntent().getStringExtra("bookColor"));
        buttonEditSOP.setOnClickListener(v -> {
            addSopTitle = editTextAddSopTitle.getText().toString();

            stepsRoomDatabase().listOfSteps().updateBookColor(bookColor,getIntent().getStringExtra(getString(R.string.editbooktitle)));
                stepsRoomDatabase().listOfSteps().updateSop(addSopTitle, getIntent().getStringExtra(getString(R.string.editbooktitle)));
                Intent returnToSOP = new Intent(this, ListofHandbooks.class);
                startActivity(returnToSOP);
                finish();
        });
    }

    private boolean checkDuplicateSOP() {
        List<StepsRoomData> SOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(categoryName, 1);


        for (int i = 0; i < SOPs.size(); i++) {
            SOPTitlesCheck = String.valueOf(SOPs.get(i).getSopTitle().toUpperCase());

            if (SOPTitlesCheck.equals(addSopTitle.toUpperCase())) {
                String sopCategory = SOPs.get(i).getCategory();
                Toast.makeText(this, getString(R.string.bookexsits) + sopCategory + getString(R.string.shelf), Toast.LENGTH_LONG).show();
                return false;
            } else if (addSopTitle.equals("")) {
                Toast.makeText(this, R.string.bookname, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void showBookColor(String bookColor){
        switch (bookColor) {
            case "Red":
                viewBookRed.setImageResource(R.drawable.description_border);
                break;
            case "Blue":
                viewBookBlue.setImageResource(R.drawable.description_border);
                break;
            case "Green":
                viewBookGreen.setImageResource(R.drawable.description_border);
                break;
            case "Yellow":
                viewBookYellow.setImageResource(R.drawable.description_border);
                break;
            case "Orange":
                viewBookOrange.setImageResource(R.drawable.description_border);
                break;
            default:
        }
    }
}
