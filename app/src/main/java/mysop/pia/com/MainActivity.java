package mysop.pia.com;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

        checkForCategories();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");
        user = FirebaseAuth.getInstance().getCurrentUser();

        getFirebaseBooks();

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
        sopList = roomDatabase().mysopDao().getAllSOPs();
        if (sopList.size() > 0) {
            setupRecylerviewDBAndAdapter();
        } else {
            imageviewNoCategory.setVisibility(View.VISIBLE);
            textviewNoCategory.setVisibility(View.VISIBLE);
        }
    }

    private void getFirebaseBooks() {
        mSopStepsDatabaseReference.child(user.getDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MySOPs sharedInfo = dataSnapshot.getValue(MySOPs.class);
                if (sharedInfo != null) {
                    sopList.add(sharedInfo);
                    categoriesRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
