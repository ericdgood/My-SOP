package mysop.pia.com.ListofHandbooks;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.ShelfRecyclerAdapter;
import mysop.pia.com.MainActivity;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;

public class ListofHandbooks extends AppCompatActivity {

    @BindView(R.id.recyclerview_list_of_sops)
    RecyclerView recyclerviewListofSOPs;
    @BindView(R.id.no_bookmarked)
    TextView tvNoBookmarks;
    @BindView(R.id.fab_addsop)
    FloatingActionButton fabAddSOP;

    List<StepsRoomData> listOfSOPs = new ArrayList<>();
    ListofHandbooksAdapter SOPsRecyclerAdapter;
    String categoryName;
    int sharedBook = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_sops);
        ButterKnife.bind(this);


        categoryName = ShelfRecyclerAdapter.categoryName;

        setTitle(categoryName + getString(R.string.handbook1));

        setupRecyclerviewAndAdapter();

        fabAddSOP.setOnClickListener(v -> {
            Intent addNewSOP = new Intent(this, AddHandbook.class);
            startActivity(addNewSOP);
            finish();
        });
    }

    @SuppressLint("RestrictedApi")
    public void getBooks() {
        if (categoryName.equals(getString(R.string.bookmarked))) {
            fabAddSOP.setVisibility(View.GONE);
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSavedBooks(1);
            if (listOfSOPs.size() == 0) {
                tvNoBookmarks.setVisibility(View.VISIBLE);
            }
        } else if (categoryName.equals(getString(R.string.sharedbooks2))) {
            fabAddSOP.setVisibility(View.GONE);
            if (listOfSOPs.size() == 0){
                tvNoBookmarks.setVisibility(View.VISIBLE);
                tvNoBookmarks.setText(R.string.sharedbooks);
            }
        } else if (sharedBook == 0) {
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(categoryName, 1);
        }
    }

    public void noBooks(){
        if (listOfSOPs.size() == 0) {
            tvNoBookmarks.setVisibility(View.VISIBLE);
            String addBook = getString(R.string.add_book_to_shelf) + categoryName;
            tvNoBookmarks.setText(addBook);
        }
    }

    private void setupRecyclerviewAndAdapter() {
        getFirebaseBooks();
        getBooks();
        noBooks();
        SOPsRecyclerAdapter = new ListofHandbooksAdapter(this, listOfSOPs, stepsRoomDatabase());
        recyclerviewListofSOPs.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListofSOPs.setAdapter(SOPsRecyclerAdapter);
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void getFirebaseBooks(){
        List<StepsRoomData> fbsteps = MainActivity.firebaseSteps;
        for (int i = 0; i < fbsteps.size(); i++) {
            int sharedStat = fbsteps.get(i).getSharedStatus();
            int pageNumber = fbsteps.get(i).getStepNumber();
            if (categoryName.equals(getString(R.string.sharedbooks2)) && sharedStat == 4 && pageNumber == 1 ||
                    categoryName.equals(getString(R.string.sharedbooks2)) && sharedStat == 5 && pageNumber == 1) {
                listOfSOPs.add(fbsteps.get(i));
                sharedBook = sharedBook + 1;
            } else if (categoryName.equals(fbsteps.get(i).getCategory()) && sharedStat == 2 && pageNumber == 1){
                listOfSOPs.add(fbsteps.get(i));
                sharedBook = sharedBook + 1;
            }
        }
    }
}
