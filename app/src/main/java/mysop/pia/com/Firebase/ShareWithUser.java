package mysop.pia.com.Firebase;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
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

    String searchUserName;

    private DatabaseReference mUsersDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;
    FirebaseUser user;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_with_users);
        ButterKnife.bind(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("page_photo");

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
//                        SEND 1 HANDBOOK
                        if (ListofSOPsAdapter.bookShare == 1) {
                            stepsRoomDatabase().listOfSteps().updateBookSharing(user.getDisplayName(), ListofSOPsAdapter.bookTitle);
                            List<StepsRoomData> book = stepsRoomDatabase().listOfSteps().getAllSteps(ListofSOPsAdapter.bookTitle);
                            sendPhotos(book);
                            mSopStepsDatabaseReference.child(searchUserName).push().setValue(book);
                            Toast.makeText(ShareWithUser.this,  ListofSOPsAdapter.bookTitle + " sent to " + searchUserName, Toast.LENGTH_LONG).show();
                        } else {
//                        SEND ALL BOOKS ON A BOOK SHELF TO USER
                            stepsRoomDatabase().listOfSteps().updateShelfSharing(user.getDisplayName(), CategoryRecyclerAdapter.categoryName);
                            List<StepsRoomData> book = stepsRoomDatabase().listOfSteps().getAllBooks(CategoryRecyclerAdapter.categoryName);
                            sendPhotos(book);
                            mSopStepsDatabaseReference.child(searchUserName).push().setValue(book);
                            Toast.makeText(ShareWithUser.this, CategoryRecyclerAdapter.categoryName + " sent to " + searchUserName, Toast.LENGTH_LONG).show();
                        }

                        Intent goToBooks = new Intent(ShareWithUser.this, MainActivity.class);
                        startActivity(goToBooks);

                    } else {
                        Toast.makeText(ShareWithUser.this, "No user by that name.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });

    }

    private void sendPhotos(List<StepsRoomData> book) {

        for (int i = 0; i < book.size(); i++) {
            String photo = book.get(i).getImageURI();

            if (photo != null) {
                Uri photoUri = Uri.parse(photo);
//                SEND PHOTO TO FIREBASE STORAGE IF BOOK CONTAINS PHOTO
                StorageReference photoRef = mChatPhotosStorageReference.child(photoUri.getLastPathSegment());

//              Upload file to Firebase Storage
                photoRef.putFile(photoUri);
            }
            }

        }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
