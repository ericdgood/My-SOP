package mysop.pia.com.Categories;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.ShelfRoom.AppDatabase;
import mysop.pia.com.Categories.ShelfRoom.MySOPs;
import mysop.pia.com.MainActivity;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.R;

public class AddShelf extends Activity {

    @BindView(R.id.editText_category_name)
    EditText editTextCategoryName;
    @BindView(R.id.button_save)
    Button buttonSave;
    @BindView(R.id.textview_new_cat_label)
    TextView tvShelfLabel;

    String categoryTitle;
    String catTitlesCheck;
    boolean edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shelf);
        ButterKnife.bind(this);

        edit = getIntent().getBooleanExtra(getString(R.string.edit1), true);

        if (edit) {
            buttonSave.setOnClickListener(v -> {
//                THIS WILL SAVE THE CATEGORY INFO
                categoryTitle = editTextCategoryName.getText().toString();

                if (checkDuplicateCategory()) {
//               DO THIS IS CATEGORY DOES NOT EXISTS
                    MySOPs category = new MySOPs(categoryTitle, null);
                    roomDatabase().mysopDao().insertAll(category);

                    Intent returnHome = new Intent(AddShelf.this, MainActivity.class);
                    startActivity(returnHome);
                    finish();
                }
            });
        } else {
            categoryTitle = getIntent().getStringExtra(getString(R.string.shelftitle));
            int id = getIntent().getIntExtra(getString(R.string.id),0);
            setTitle(getString(R.string.editshelf));
            editTextCategoryName.setText(categoryTitle);
            tvShelfLabel.setText(R.string.edit_shelfName);
            buttonSave.setText(R.string.edit);

            buttonSave.setOnClickListener(v -> {
                categoryTitle = editTextCategoryName.getText().toString();
            if (checkDuplicateCategory()){
                roomDatabase().mysopDao().updateShelf(categoryTitle,id);
                pagesRoom().listOfSteps().updatePagesShelf(categoryTitle, getIntent().getStringExtra(getString(R.string.shelftitle)));

                Intent returnHome = new Intent(AddShelf.this, MainActivity.class);
                startActivity(returnHome);
                finish();
            }
            });
        }
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.mysop))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public StepsAppDatabase pagesRoom() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private boolean checkDuplicateCategory(){
        List<MySOPs> categories = roomDatabase().mysopDao().getAllSOPs();

        for (int i = 0; i < categories.size(); i++) {
            catTitlesCheck = String.valueOf(categories.get(i).getCategoryTitle().toUpperCase());

            if (catTitlesCheck.equals(categoryTitle.toUpperCase())) {
                Toast.makeText(this, R.string.catexsits, Toast.LENGTH_LONG).show();
                return false;
            } else if (categoryTitle.equals("")){
                Toast.makeText(this, R.string.newcat, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnhome = new Intent(this, MainActivity.class);
        startActivity(returnhome);
    }
}
