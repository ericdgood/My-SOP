package mysop.pia.com.Categories;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.MainActivity;
import mysop.pia.com.R;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;

public class AddCategory extends Activity {

    @BindView(R.id.editText_category_name)
    EditText editTextCategoryName;
    @BindView(R.id.button_save)
    Button buttonSave;
    MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        ButterKnife.bind(this);

        buttonSave.setOnClickListener(v -> {
            String categoryTitle = editTextCategoryName.getText().toString();


//                THIS WILL SAVE THE CATEGORY INFO
            MySOPs category = new MySOPs(categoryTitle);
            roomDatabase().mysopDao().insertAll(category);

            Intent returnHome = new Intent(AddCategory.this, MainActivity.class);
            startActivity(returnHome);
            finish();
        });
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mysop")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

}
