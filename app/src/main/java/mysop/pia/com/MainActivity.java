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
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.AddCategory;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActvity";
    private List<MySOPs> sopList = new ArrayList<>();

    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerViewCategories;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.imageview_sop_logo_no_categories)
    ImageView imageviewNoCategory;
    @BindView(R.id.textview_no_categories)
    TextView textviewNoCategory;
    @BindView(R.id.framelayout_category_options_frag)
    FrameLayout mCatOptionsFrag;

    CategoryRecyclerAdapter categoriesRecyclerAdapter;
    FirebaseUser user;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;

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

        checkForCategories();

        fab.setOnClickListener(view -> {
            Intent addCategory = new Intent(MainActivity.this, AddCategory.class);
            startActivity(addCategory);
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
        if (id == R.id.edit_category) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecylerviewDBAndAdapter() {
//      SETUP RECYCLERVIEW AND ADAPTER
        categoriesRecyclerAdapter = new CategoryRecyclerAdapter(sopList, this, mCatOptionsFrag, roomDatabase());
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
        sopList = roomDatabase().mysopDao().getAllSOPs();
        getFirebaseBooks();
        if (sopList.size() > 0) {
            setupRecylerviewDBAndAdapter();
        } else {
            imageviewNoCategory.setVisibility(View.VISIBLE);
            textviewNoCategory.setVisibility(View.VISIBLE);
        }
    }

    private void getFirebaseBooks() {
        mSopStepsDatabaseReference.child(user.getDisplayName()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MySOPs sharedInfo = dataSnapshot.getValue(MySOPs.class);
                if (sharedInfo.getSharedAuthor() != null) {
                    alertToDelete(sharedInfo.getCategoryTitle(), sharedInfo.getSharedAuthor(), sharedInfo);

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
    }

    private void alertToDelete(String categoryTitle, String sharedAuthor, MySOPs sharedInfo) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle("Shared Book")
                .setMessage(sharedAuthor + " would like to share " + categoryTitle + " handbook with you" )
                .setPositiveButton("Accept", (dialog, which) -> {
//                    DO THIS WHEN RECEIVE A SHARED BOOK
                    sopList.add(sharedInfo);
                    categoriesRecyclerAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Item added to list", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Deny", (dialog, which) -> {
                    // do nothing
                    Toast.makeText(MainActivity.this, "Item not accepted", Toast.LENGTH_SHORT).show();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
