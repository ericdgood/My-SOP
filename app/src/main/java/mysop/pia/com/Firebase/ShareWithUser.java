package mysop.pia.com.Firebase;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.ListofSOPsAdapter;
import mysop.pia.com.MainActivity;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class ShareWithUser extends AppCompatActivity {

    @BindView(R.id.edittext_share_search_users)
    EditText etSearchUsers;
    @BindView(R.id.button_share_search)
    Button btnSearch;
    @BindView(R.id.tv_share_display_name)
    TextView tvDisplayUsername;

    String bookTitle;
    String searchUserName;

    private DatabaseReference mUsersDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_with_users);
        ButterKnife.bind(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");
        user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        String displayName = "User name is " + user.getDisplayName();
        tvDisplayUsername.setText(displayName);

        btnSearch.setOnClickListener(v -> {
            searchUserName = etSearchUsers.getText().toString().toLowerCase();

            com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild("userName").equalTo(searchUserName);

            userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        bookTitle = ListofSOPsAdapter.bookTitle;
                        List<StepsRoomData> book = stepsRoomDatabase().listOfSteps().getAllSteps(bookTitle);
//
//                        List<SharedBook> bookPages = new ArrayList<>();
//                        for (int i = 0; i < book.size(); i++) {
//                            String pageTitle = book.get(i).getStepTitle();
//                            int pageNum = book.get(i).getStepNumber();
//                            String pageImg = book.get(i).getImageURI();
//                            String pageDescription = book.get(i).getStepDescription();
//
//                            SharedBook sharedPages = new SharedBook(pageTitle, pageNum, pageImg, pageDescription);
//                            bookPages.add(sharedPages);
//                        }

                        Map bookInfo = new HashMap();
                        bookInfo.put("author", user.getDisplayName());
                            mSopStepsDatabaseReference.child(searchUserName).child("test").setValue(book);
//                        DatabaseReference path = mSopStepsDatabaseReference.child(searchUserName).push().child(bookTitle);
//                        path.child("bookPages").setValue(bookPages);
//                        path.child("bookInfo").setValue(bookInfo);

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

    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
