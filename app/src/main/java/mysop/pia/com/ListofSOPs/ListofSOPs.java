package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.R;
import mysop.pia.com.RoomData.SopRoom.SOPRoomData;
import mysop.pia.com.RoomData.SopRoom.SopAppDatabase;

public class ListofSOPs extends AppCompatActivity {

    @BindView(R.id.recyclerview_list_of_sops)
    RecyclerView recyclerviewListofSOPs;
    @BindView(R.id.fab_addsop)
    FloatingActionButton fabAddSOP;

    private List<SOPRoomData> listOfSOPs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_sops);
        ButterKnife.bind(this);

        setupRecyclerviewAndAdapter();

        fabAddSOP.setOnClickListener(v -> {
            Intent addNewSOP = new Intent(this, AddSOP.class);
            startActivity(addNewSOP);
            finish();
        });

    }

    private void setupRecyclerviewAndAdapter(){
        listOfSOPs = sopRoomDatabase().listOfSOPs().getAllSOPs(CategoryRecyclerAdapter.categoryName);
        ListofSOPsAdapter SOPsRecyclerAdapter = new ListofSOPsAdapter(this, listOfSOPs);
        recyclerviewListofSOPs.setLayoutManager(new LinearLayoutManager(this));
        recyclerviewListofSOPs.setAdapter(SOPsRecyclerAdapter);
    }

    public SopAppDatabase sopRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), SopAppDatabase.class, "sopinfo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
