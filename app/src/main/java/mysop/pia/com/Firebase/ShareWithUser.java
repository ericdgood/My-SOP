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
import mysop.pia.com.Categories.ShelfRecyclerAdapter;
import mysop.pia.com.Categories.ShelfRoom.AppDatabase;
import mysop.pia.com.ListofHandbooks.ListofHandbooksAdapter;
import mysop.pia.com.MainActivity;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;

public class ShareWithUser extends AppCompatActivity {

    @BindView(R.id.edittext_share_search_users)
    EditText etSearchUsers;
    @BindView(R.id.button_share_search)
    Button btnSearch;
    @BindView(R.id.tv_share_display_name)
    TextView tvDisplayUsername;

    String searchUserName;

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mSopStepsDatabaseReference;
    FirebaseUser user;
    private StorageReference mChatPhotosStorageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_with_users);
        ButterKnife.bind(this);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.users));
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.sop));
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child(getString(R.string.pagephoto));

        assert user != null;
        String displayName = getString(R.string.usernameis) + user.getDisplayName();
        tvDisplayUsername.setText(displayName);

        btnSearch.setOnClickListener(v -> {
            searchUserName = etSearchUsers.getText().toString().toLowerCase();

            com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild(getString(R.string.username)).equalTo(searchUserName);

            userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
//                        SEND 1 HANDBOOK
                        if (ListofHandbooksAdapter.bookShare == 1) {
                            stepsRoomDatabase().listOfSteps().updateBookSharing(user.getDisplayName(), ListofHandbooksAdapter.bookTitle);
                            List<StepsRoomData> book = stepsRoomDatabase().listOfSteps().getAllSteps(ListofHandbooksAdapter.bookTitle);
                            sendPhotos(book);
                            mSopStepsDatabaseReference.child(searchUserName).push().setValue(book);
                            Toast.makeText(ShareWithUser.this,  ListofHandbooksAdapter.bookTitle + getString(R.string.sent_to) + searchUserName, Toast.LENGTH_LONG).show();
                        } else {
//                        SEND ALL BOOKS ON A BOOK SHELF TO USER
                            stepsRoomDatabase().listOfSteps().updateShelfSharing(user.getDisplayName(), ShelfRecyclerAdapter.categoryName);
                            roomDatabase().mysopDao().updateSharedShelf(ShelfRecyclerAdapter.categoryName);
                            List<StepsRoomData> book = stepsRoomDatabase().listOfSteps().getAllBooks(ShelfRecyclerAdapter.categoryName);
                            sendPhotos(book);
                            mSopStepsDatabaseReference.child(searchUserName).push().setValue(book);
                            Toast.makeText(ShareWithUser.this, ShelfRecyclerAdapter.categoryName + getString(R.string.sent_to) + searchUserName, Toast.LENGTH_LONG).show();
                        }

                        Intent goToBooks = new Intent(ShareWithUser.this, MainActivity.class);
                        startActivity(goToBooks);

                    } else {
                        Toast.makeText(ShareWithUser.this, R.string.no_user, Toast.LENGTH_SHORT).show();
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
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.mysop))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
