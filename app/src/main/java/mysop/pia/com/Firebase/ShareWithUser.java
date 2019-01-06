package mysop.pia.com.Firebase;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.MainActivity;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ShareWithUser extends AppCompatActivity {

    @BindView(R.id.edittext_share_search_users)
    EditText etSearchUsers;
    @BindView(R.id.button_share_search)
    Button btnSearch;
    @BindView(R.id.recyclerview_share)
    RecyclerView rvShare;

    String searchUserName;
    List<SharedInfo> sharedInfo = new ArrayList<>();

    List<StepsRoomData> sopSteps = new ArrayList<>();
    private ShareFbAdapter recyclerAdapter;
    CategoryRecyclerAdapter catRecycler;

    private DatabaseReference mUsersDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;
    private ChildEventListener mChildEventListener;
    FirebaseUser user;
    private String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_with_users);
        ButterKnife.bind(this);

//        recyclerAdapter = new ShareFbAdapter(this, sharedInfo);
//        rvShare.setLayoutManager(new LinearLayoutManager(this));
//        rvShare.setAdapter(recyclerAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnSearch.setOnClickListener(v -> {
            searchUserName = etSearchUsers.getText().toString().toLowerCase();

            com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild("userName").equalTo(searchUserName);

            userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        sopSteps = stepsDB().listOfSteps().getAllSteps("Sop");
                        String sopTitleTest = "CAT TEST";
//
//                        Map sopShare = new HashMap();
//                        sopShare.put("sopTitle", sopTitleTest);

//                        SharedInfo sendSharedInfo = new SharedInfo(user.getDisplayName(), searchUserName, sopTitleTest);
                        MySOPs sharedCat = new MySOPs(sopTitleTest);

                        mSopStepsDatabaseReference.push().setValue(sharedCat);

                        Intent goToBooks = new Intent(ShareWithUser.this, MainActivity.class);
                        startActivity(goToBooks);
                        Toast.makeText(ShareWithUser.this, "Found User", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ShareWithUser.this, "No user by that name", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot sopReturn, @Nullable String s) {
                MySOPs sharedInfo = sopReturn.getValue(MySOPs.class);
//                if (sharedInfo.getSharedUser().equals(user.getDisplayName())) {
                    addItem(sharedInfo);
//                }
//                else {
//                    Toast.makeText(ShareWithUser.this, "No shared info", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mSopStepsDatabaseReference.addChildEventListener(mChildEventListener);
    }

    public StepsAppDatabase stepsDB() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void addItem(MySOPs recInfo) {
//        sharedInfo.add(recInfo);
//        recyclerAdapter.notifyDataSetChanged();
        roomDatabase().mysopDao().insertAll(recInfo);
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mysop")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
