package mysop.pia.com.ListofSOPs;

import android.app.AlertDialog;
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

        new SwipeHelper(this, recyclerviewListofSOPs) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        getColor(R.color.logoRedBookColor),
                        pos -> {
                            sopTitle = listOfSOPs.get(viewHolder.getAdapterPosition()).getSopTitle();
                            alertBoxTitle = "Delete SOP";
                            alertBoxMessage = "Are you sure you want to delete " + sopTitle + " SOP?";
                            alertToDelete(alertBoxTitle, alertBoxMessage, viewHolder);
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Edit",
                        getColor(R.color.logoBlueBookColor),
                        pos -> {
                            editID = listOfSOPs.get(viewHolder.getAdapterPosition()).getId();
                            sopTitle = listOfSOPs.get(viewHolder.getAdapterPosition()).getSopTitle();
                            alertBoxTitle = "Edit SOP";
                            alertBoxMessage = "Are you sure you would like to edit " + sopTitle;
                            alertToDelete(alertBoxTitle, alertBoxMessage, viewHolder);
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Share",
                        getColor(R.color.logoYellowBookColor),
                        pos -> {
                            // TODO: Shared
                            alertBoxTitle = "Share SOP";
                            alertToDelete(alertBoxTitle, alertBoxMessage, viewHolder);
                        }
                ));
            }
        };
    }

    private void alertToDelete(String alertBoxTitle, String alertBoxMessage, RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle(alertBoxTitle)
                .setMessage(alertBoxMessage)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        DO STEP HERE
                    if (alertBoxTitle.equals("Delete SOP")) {
                        stepsRoomDatabase().listOfSteps().DeleteSOP(sopTitle);
                        listOfSOPs.remove(listOfSOPs.get(viewHolder.getAdapterPosition()));
                        SOPsRecyclerAdapter.notifyDataSetChanged();
                        Toast.makeText(this, sopTitle + " is deleted from book", Toast.LENGTH_SHORT).show();
                    } else if (alertBoxTitle.equals("Edit SOP")) {
                        editSOP();
                        Toast.makeText(this, "Edit SOP", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(this, "Share SOP", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
//        return position;
    }

    private void editSOP(){
        Intent editSOP = new Intent(this, AddSOP.class);
        editSOP.putExtra("editSop",1);
        editSOP.putExtra("editSopTitle", sopTitle);
        editSOP.putExtra("editId", editID);
        startActivity(editSOP);
    }

    private void setUpPage() {
        String titleConcat = "List of " + CategoryRecyclerAdapter.categoryName + " SOPs";
        textviewCategoryListTitle.setText(titleConcat);
    }

    private void setupRecyclerviewAndAdapter() {
        listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(CategoryRecyclerAdapter.categoryName);
        SOPsRecyclerAdapter = new ListofSOPsAdapter(this, listOfSOPs, stepsRoomDatabase(), recyclerviewListofSOPs);
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
