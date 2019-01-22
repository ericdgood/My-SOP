package mysop.pia.com;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.AddCategory;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;
import mysop.pia.com.Firebase.Firebase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "test";
    public static List<MySOPs> sopList = new ArrayList<>();
    public static List<StepsRoomData> firebaseSteps = new ArrayList<>();

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerViewCategories;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.imageview_sop_logo_no_categories)
    ImageView imageviewNoCategory;
    @BindView(R.id.textview_no_categories)
    TextView textviewNoCategory;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    CategoryRecyclerAdapter categoriesRecyclerAdapter;
    FirebaseUser user;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;
    String firebaseShelfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");
        user = FirebaseAuth.getInstance().getCurrentUser();

        sopList = roomDatabase().mysopDao().getAllSOPs();
        getFirebaseBooks();
        checkForCategories();

        fab.setOnClickListener(view -> {
            Intent addCategory = new Intent(MainActivity.this, AddCategory.class);
            startActivity(addCategory);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_in) {
            Intent share = new Intent(this, Firebase.class);
            startActivity(share);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecylerviewDBAndAdapter() {
//      SETUP RECYCLERVIEW AND ADAPTER
        categoriesRecyclerAdapter = new CategoryRecyclerAdapter(sopList, this, roomDatabase());
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCategories.setAdapter(categoriesRecyclerAdapter);
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mysop")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void checkForCategories() {
        if (sopList.size() > 0 || user != null) {
            arrangeCategoryTitles();
            staticBookShelfs();
            setupRecylerviewDBAndAdapter();
        } else {
            imageviewNoCategory.setVisibility(View.VISIBLE);
            String creatBookShelf = "Create a book shelf \n to keep your handbooks organized";
            textviewNoCategory.setText(creatBookShelf);
            textviewNoCategory.setVisibility(View.VISIBLE);
            staticBookShelfs();
            setupRecylerviewDBAndAdapter();
        }
    }


    private void staticBookShelfs() {
        MySOPs bookMarked = new MySOPs("Bookmarked", "JonNyBgOoDeMARKED");
        MySOPs shared = new MySOPs("Shared Books", "JonNyBgOoDeSHARED");
        sopList.add(0, shared);
        sopList.add(1, bookMarked);
    }

    private void arrangeCategoryTitles() {
        Collections.sort(sopList, (item, t1) -> {
            String s1 = item.getCategoryTitle();
            String s2 = t1.getCategoryTitle();
            return s1.compareToIgnoreCase(s2);
        });
    }

    private void getFirebaseBooks() {
        if (user != null) {
            mSopStepsDatabaseReference.child(user.getDisplayName()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        StepsRoomData stringValue = ds.getValue(StepsRoomData.class);

                        progressBar.setVisibility(View.GONE);
                        firebaseSteps.add(stringValue);

                        assert stringValue != null;
                        if (stringValue.getSharedStatus() == 1 || stringValue.getSharedStatus() == 4 && stringValue.getStepNumber() == 1) {
                            alertSharedShelf(stringValue, ds);
                        } else if (stringValue.getSharedStatus() == 2) {
                            addSharedShelfs(stringValue);
                        }
                    }
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
            });
        } else {
//            DO THIS IS USER IS NULL
            progressBar.setVisibility(View.GONE);
        }
    }

    private void alertSharedShelf(StepsRoomData sharedBook, DataSnapshot ds) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("Shared Book")
                .setMessage(" would you like to receive " + sharedBook.getSopTitle() + " Handbook from " + sharedBook.getSharedAuthor())
                .setPositiveButton("Accept", (dialog, which) -> {
//                    DO THIS WHEN RECEIVE A SHARED BOOK

                    if (sharedBook.getSharedStatus() == 1) {
                        ds.getRef().child("sharedStatus").setValue(2);
                        addSharedShelfs(sharedBook);
                    }
                    if (sharedBook.getSharedStatus() == 4){
                        ds.getRef().child("sharedStatus").setValue(5);
                    }

                })
                .setNegativeButton("Deny", (dialog, which) -> {
                    // do nothing
                    ds.getRef().child("sharedStatus").setValue(3);
                    Toast.makeText(MainActivity.this, "Item not accepted", Toast.LENGTH_SHORT).show();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void addSharedShelfs(StepsRoomData sharedBook) {
        if (sharedBook.getStepNumber() == 1 && !sharedBook.getCategory().equals(firebaseShelfs)) {
            firebaseShelfs = sharedBook.getCategory();
            MySOPs book = new MySOPs(firebaseShelfs, sharedBook.getSharedAuthor());
            sopList.add(book);
            categoriesRecyclerAdapter.notifyDataSetChanged();
        }
    }


}
