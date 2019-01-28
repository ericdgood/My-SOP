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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.AddShelf;
import mysop.pia.com.Categories.ShelfRecyclerAdapter;
import mysop.pia.com.Categories.ShelfRoom.AppDatabase;
import mysop.pia.com.Categories.ShelfRoom.MySOPs;
import mysop.pia.com.Firebase.Firebase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity extends AppCompatActivity {

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

    ShelfRecyclerAdapter categoriesRecyclerAdapter;
    FirebaseUser user;
    private DatabaseReference mSopStepsDatabaseReference;
    String firebaseShelfs;
    StepsRoomData page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.sop));
        user = FirebaseAuth.getInstance().getCurrentUser();

        sopList = roomDatabase().mysopDao().getAllSOPs();
        getFirebaseBooks();
        checkForCategories();

        fab.setOnClickListener(view -> {
            Intent addCategory = new Intent(MainActivity.this, AddShelf.class);
            startActivity(addCategory);
            customType(MainActivity.this,"left-to-right");
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
            share.putExtra(getString(R.string.signin), 1);
            startActivity(share);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecylerviewDBAndAdapter() {
//      SETUP RECYCLERVIEW AND ADAPTER
        categoriesRecyclerAdapter = new ShelfRecyclerAdapter(sopList, this, roomDatabase());
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCategories.setAdapter(categoriesRecyclerAdapter);
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.mysop))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void staticBookShelfs() {
        MySOPs bookMarked = new MySOPs(getString(R.string.bookmarked), getString(R.string.bookmarkpass));
        MySOPs shared = new MySOPs(getString(R.string.sharedbooks1), getString(R.string.sharedpass));
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

    public void checkForCategories() {
        if (sopList.size() > 0) {
            arrangeCategoryTitles();
            staticBookShelfs();
            setupRecylerviewDBAndAdapter();
        } else {
            imageviewNoCategory.setVisibility(View.VISIBLE);
            String creatBookShelf = getString(R.string.new_bookshelf);
            textviewNoCategory.setText(creatBookShelf);
            textviewNoCategory.setVisibility(View.VISIBLE);
            staticBookShelfs();
            setupRecylerviewDBAndAdapter();
        }
    }

    private void getFirebaseBooks() {
        if (user != null) {
            mSopStepsDatabaseReference.child(Objects.requireNonNull(user.getDisplayName())).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        page = ds.getValue(StepsRoomData.class);

                        int key = Integer.parseInt(ds.getKey());
                        assert page != null;
                        if (page.getSharedStatus() == 1 && page.getStepNumber() == 1) {
                            if (key == 0) {
                                alertSharedShelf(page, dataSnapshot);
                            }
                        }
                        if (page.getSharedStatus() == 4 && page.getStepNumber() == 1) {
//                               DO THIS IF BOOK IS SHARED
                            alertSharedShelf(page, dataSnapshot);
                        }
                        if (page.getSharedStatus() == 2 ||
                                page.getSharedStatus() == 5) {
                            addSharedShelfs(page);
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
        }  //            DO THIS IS USER IS NULL

    }

    private void alertSharedShelf(StepsRoomData sharedBook, DataSnapshot dataSnapshot) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        builder.setTitle(R.string.sharedbook)
                .setMessage(getString(R.string.receive) + sharedBook.getSopTitle() + getString(R.string.bookfrom) + sharedBook.getSharedAuthor())
                .setPositiveButton("Accept", (dialog, which) -> {
//                    DO THIS WHEN RECEIVE A SHARED BOOK

                    sharedStat(dataSnapshot, true);

                    Intent reload = new Intent(this, MainActivity.class);
                    firebaseSteps.clear();
                    startActivity(reload);
                    finish();
                })
                .setNegativeButton("Deny", (dialog, which) -> {
                    // do nothing
                    sharedStat(dataSnapshot, false);
                    Toast.makeText(MainActivity.this, R.string.not_accepted, Toast.LENGTH_SHORT).show();
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        if (!this.isFinishing()) {

            builder.show();
        }
    }

    private void addSharedShelfs(StepsRoomData sharedBook) {
        firebaseSteps.add(sharedBook);
        if (sharedBook.getStepNumber() == 1 && !sharedBook.getCategory().equals(firebaseShelfs)
                && !sharedBook.getCategory().equals(getString(R.string.sharedbooks2))) {
            firebaseShelfs = sharedBook.getCategory();
            MySOPs book = new MySOPs(firebaseShelfs, sharedBook.getSharedAuthor());
            sopList.add(book);
            categoriesRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private void sharedStat(DataSnapshot dataSnapshot, boolean accept) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            page = ds.getValue(StepsRoomData.class);
            if (accept) {
                assert page != null;
                if (page.getSharedStatus() == 1) {
                    ds.getRef().child(getString(R.string.sharedstatus)).setValue(2);
                }
                if (page.getSharedStatus() == 4) {
                    ds.getRef().child(getString(R.string.category)).setValue(getString(R.string.sharedbooks2));
                    ds.getRef().child(getString(R.string.sharedstatus)).setValue(5);
                }
            } else {
                ds.getRef().removeValue();
            }
        }
    }
}
