package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListofSOPs extends AppCompatActivity {

    private static final String TAG = "Hello";
    @BindView(R.id.recyclerview_list_of_sops)
    RecyclerView recyclerviewListofSOPs;
    @BindView(R.id.listsop_categorytitle)
    TextView textviewCategoryListTitle;
    @BindView(R.id.fab_addsop)
    FloatingActionButton fabAddSOP;
    private String alertBoxTitle;
    private String alertBoxMessage;
    String sopTitle;
    int editID;

    List<StepsRoomData> listOfSOPs = new ArrayList<>();
    ListofSOPsAdapter SOPsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_sops);
        ButterKnife.bind(this);

        setUpPage();

        setupRecyclerviewAndAdapter();

        fabAddSOP.setOnClickListener(v -> {
            Intent addNewSOP = new Intent(this, AddSOP.class);
            startActivity(addNewSOP);
            finish();
        });
    }

    private void setUpPage() {
        String titleConcat = "List of " + CategoryRecyclerAdapter.categoryName + " SOPs";
        textviewCategoryListTitle.setText(titleConcat);
    }

    public void getBooks(){
        if (CategoryRecyclerAdapter.categoryName.equals("Bookmarked")){
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSavedBooks(1);
            if (listOfSOPs.size() == 0){
                textviewCategoryListTitle.setText("No Bookmarked Handbooks");
            }
        } else {
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(CategoryRecyclerAdapter.categoryName);
        }
    }

    private void setupRecyclerviewAndAdapter() {
        getBooks();
        SOPsRecyclerAdapter = new ListofSOPsAdapter(this, listOfSOPs, stepsRoomDatabase());
        recyclerviewListofSOPs.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListofSOPs.setAdapter(SOPsRecyclerAdapter);
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

}
