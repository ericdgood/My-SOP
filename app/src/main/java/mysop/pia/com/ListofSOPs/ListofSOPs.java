package mysop.pia.com.ListofSOPs;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ListofSOPs extends AppCompatActivity {

    @BindView(R.id.recyclerview_list_of_sops)
    RecyclerView recyclerviewListofSOPs;
    @BindView(R.id.no_bookmarked)
    TextView tvNoBookmarks;
    @BindView(R.id.fab_addsop)
    FloatingActionButton fabAddSOP;

    List<StepsRoomData> listOfSOPs = new ArrayList<>();
    ListofSOPsAdapter SOPsRecyclerAdapter;
    String categoryName;
    private String TAG;

    FirebaseUser user;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_sops);
        ButterKnife.bind(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop").child(user.getDisplayName());

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
    public void getBooks(){
        if (categoryName.equals("Bookmarked")){
        fabAddSOP.setVisibility(View.GONE);
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSavedBooks(1);
            if (listOfSOPs.size() == 0){
                tvNoBookmarks.setVisibility(View.VISIBLE);
            }
        }
        else if (categoryName.equals("Shared Books")){
            fabAddSOP.setVisibility(View.GONE);
            getFirebaseBooks();
//            TODO: DO THIS IF NO SHARED BOOKS
//            if (listOfSOPs.size() == 0){
//                tvNoBookmarks.setVisibility(View.VISIBLE);
//                tvNoBookmarks.setText("No shared Handbooks");
//            }
        }
        else {
            listOfSOPs = stepsRoomDatabase().listOfSteps().getAllSOPs(categoryName);
            if (listOfSOPs.size() == 0){
                tvNoBookmarks.setVisibility(View.VISIBLE);
                tvNoBookmarks.setText("Add a Handbook to " + categoryName);
            }
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

    private void getFirebaseBooks() {
        mSopStepsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StepsRoomData book = dataSnapshot.child("test").child("0").getValue(StepsRoomData.class);
                listOfSOPs.add(book);
                SOPsRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
