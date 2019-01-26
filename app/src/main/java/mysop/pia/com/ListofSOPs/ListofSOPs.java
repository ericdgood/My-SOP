package mysop.pia.com.ListofSOPs;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.MainActivity;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListofSOPs extends AppCompatActivity {

    private static final String TAG = "testing";
    @BindView(R.id.recyclerview_list_of_sops)
    RecyclerView recyclerviewListofSOPs;
    @BindView(R.id.no_bookmarked)
    TextView tvNoBookmarks;
    @BindView(R.id.fab_addsop)
    FloatingActionButton fabAddSOP;

    List<StepsRoomData> listOfSOPs = new ArrayList<>();
    ListofSOPsAdapter SOPsRecyclerAdapter;
    String categoryName;
    int sharedBook = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_sops);
        ButterKnife.bind(this);


        categoryName = CategoryRecyclerAdapter.categoryName;

        setTitle(categoryName + " Handbooks");

        setupRecyclerviewAndAdapter();

        fabAddSOP.setOnClickListener(v -> {
            Intent addNewSOP = new Intent(this, AddSOP.class);
            startActivity(addNewSOP);
            finish();
        });
    }

    @SuppressLint("RestrictedApi")
    public void getBooks() {
        if (categoryName.equals("Bookmarked")) {
            fabAddSOP.setVisibility(View.GONE);
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSavedBooks(1);
            if (listOfSOPs.size() == 0) {
                tvNoBookmarks.setVisibility(View.VISIBLE);
            }
        } else if (categoryName.equals("Shared Books")) {
            fabAddSOP.setVisibility(View.GONE);
            if (listOfSOPs.size() == 0){
                tvNoBookmarks.setVisibility(View.VISIBLE);
                tvNoBookmarks.setText("No shared Handbooks");
            }
        } else if (sharedBook == 0) {
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(categoryName, 1);
        }
    }

    public void noBooks(){
        if (listOfSOPs.size() == 0) {
            tvNoBookmarks.setVisibility(View.VISIBLE);
            tvNoBookmarks.setText("Add a Handbook to " + categoryName);
        }
    }

    private void setupRecyclerviewAndAdapter() {
        getFirebaseBooks();
        getBooks();
        noBooks();
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

    private void getFirebaseBooks(){
        List<StepsRoomData> fbsteps = MainActivity.firebaseSteps;
        for (int i = 0; i < fbsteps.size(); i++) {
            int sharedStat = fbsteps.get(i).getSharedStatus();
            int pageNumber = fbsteps.get(i).getStepNumber();
            if (categoryName.equals("Shared Books") && sharedStat == 4 && pageNumber == 1 ||
                    categoryName.equals("Shared Books") && sharedStat == 5 && pageNumber == 1) {
                listOfSOPs.add(fbsteps.get(i));
                sharedBook = sharedBook + 1;
            } else if (categoryName.equals(fbsteps.get(i).getCategory()) && sharedStat == 2 && pageNumber == 1){
                listOfSOPs.add(fbsteps.get(i));
                sharedBook = sharedBook + 1;
                Log.i(TAG, "get shared self: " + listOfSOPs);
            }
        }
    }
}
