package mysop.pia.com.Categories;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.MainActivity;
import mysop.pia.com.R;

public class AddCategory extends Activity {

    @BindView(R.id.editText_category_name)
    EditText editTextCategoryName;
    @BindView(R.id.button_save)
    Button buttonSave;
    String categoryTitle;
    String catTitlesCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shelf);
        ButterKnife.bind(this);

        buttonSave.setOnClickListener(v -> {
//                THIS WILL SAVE THE CATEGORY INFO
            categoryTitle = editTextCategoryName.getText().toString();

                if (checkDuplicateCategory()) {
//               DO THIS IS CATEGORY DOES NOT EXISTS
                    MySOPs category = new MySOPs(categoryTitle, null);
                    roomDatabase().mysopDao().insertAll(category);

                    Intent returnHome = new Intent(AddCategory.this, MainActivity.class);
                    startActivity(returnHome);
                    finish();
                }
        });
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mysop")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private boolean checkDuplicateCategory(){
        List<MySOPs> categories = roomDatabase().mysopDao().getAllSOPs();

        for (int i = 0; i < categories.size(); i++) {
            catTitlesCheck = String.valueOf(categories.get(i).getCategoryTitle().toUpperCase());

            if (catTitlesCheck.equals(categoryTitle.toUpperCase())) {
                Toast.makeText(this, "This category already exists", Toast.LENGTH_LONG).show();
                return false;
            } else if (categoryTitle.equals("")){
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
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
