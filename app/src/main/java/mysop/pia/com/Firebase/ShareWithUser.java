package mysop.pia.com.Firebase;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ShareWithUser extends AppCompatActivity {

    @BindView(R.id.edittext_share_search_users)
    EditText etSearchUsers;
    @BindView(R.id.button_share_search)
    Button btnSearch;

    String searchUserName;

    List<StepsRoomData> sopSteps = new ArrayList<>();

    private DatabaseReference mUsersDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_with_users);
        ButterKnife.bind(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");

        btnSearch.setOnClickListener(v -> {
            searchUserName = etSearchUsers.getText().toString().toLowerCase();

            com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild("userName").equalTo(searchUserName);

            userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                         sopSteps = stepsDB().listOfSteps().getAllSteps("sop");
                        String sopTitleTest = sopSteps.get(0).getSopTitle();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        Map sopShare = new HashMap();
                        sopShare.put("sopTitle", sopTitleTest);
                        sopShare.put("sharedUser", searchUserName);
                        sopShare.put("Author", user.getDisplayName());

                        mSopStepsDatabaseReference.push().setValue(sopShare);
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
    }

    public StepsAppDatabase stepsDB() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
