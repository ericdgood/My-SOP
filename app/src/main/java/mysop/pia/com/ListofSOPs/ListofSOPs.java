package mysop.pia.com.ListofSOPs;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

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

        new SwipeHelper(this, recyclerviewListofSOPs) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        getColor(R.color.logoRedBookColor),
                        pos -> {
                            // TODO: onDelete
                            Toast.makeText(ListofSOPs.this, " Item Deleted", Toast.LENGTH_SHORT).show();
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        getColor(R.color.logoBlueBookColor),
                        pos -> {
                            // TODO: Edit
                            Toast.makeText(ListofSOPs.this, " Item Edit", Toast.LENGTH_SHORT).show();
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Share",
                        getColor(R.color.logoYellowBookColor),
                        pos -> {
                            // TODO: Shared
                            Toast.makeText(ListofSOPs.this, " Item Shared", Toast.LENGTH_SHORT).show();
                        }
                ));
            }
        };
    }

    private void setUpPage() {
        String titleConcat = "List of " + CategoryRecyclerAdapter.categoryName + " SOPs";
        textviewCategoryListTitle.setText(titleConcat);
    }

    private void setupRecyclerviewAndAdapter() {
        listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(CategoryRecyclerAdapter.categoryName);
        SOPsRecyclerAdapter = new ListofSOPsAdapter(this, listOfSOPs);
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
