package mysop.pia.com.Categories;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.R;

public class CategoryOptionsFrag extends android.support.v4.app.Fragment {

    @BindView(R.id.editText_category_name)
    EditText etCategoryName;
    @BindView(R.id.button_save)

    Button buttonSave;
    String categoryTitle;
    String catTitlesCheck;

    private Context context;
    CategoryRecyclerAdapter categoriesRecyclerAdapter;


    public CategoryOptionsFrag() {
    }

    public void getCategoryOptionsFrag(Context context){
        this.context =context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create view
        View rootView = inflater.inflate(R.layout.add_shelf, container, false);
        ButterKnife.bind(this, rootView);

        buttonSave.setOnClickListener(v -> {
//                THIS WILL SAVE THE CATEGORY INFO
            categoryTitle = etCategoryName.getText().toString();

            if (checkDuplicateCategory()) {
//               DO THIS IS CATEGORY DOES NOT EXISTS
                MySOPs category = new MySOPs(categoryTitle, null);
                roomDatabase().mysopDao().insertAll(category);
                categoriesRecyclerAdapter.notifyDataSetChanged();
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(context, AppDatabase.class, "mysop")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private boolean checkDuplicateCategory(){
        List<MySOPs> categories = roomDatabase().mysopDao().getAllSOPs();

        for (int i = 0; i < categories.size(); i++) {
            catTitlesCheck = String.valueOf(categories.get(i).getCategoryTitle().toUpperCase());

            if (catTitlesCheck.equals(categoryTitle.toUpperCase())) {
                Toast.makeText(context, "This category already exists", Toast.LENGTH_LONG).show();
                return false;
            } else if (categoryTitle.equals("")){
                Toast.makeText(context, "Please enter a category name", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
