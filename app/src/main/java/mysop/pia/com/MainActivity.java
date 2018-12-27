package mysop.pia.com;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.AddCategory;
import mysop.pia.com.Categories.CategoryRecyclerAdapter;
import mysop.pia.com.Categories.CatergoryRoom.AppDatabase;
import mysop.pia.com.Categories.CatergoryRoom.MySOPs;

public class MainActivity extends AppCompatActivity {

    private List<MySOPs> sopList = new ArrayList<>();
    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerViewCategories;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        setupRecylerviewDBAndAdapter();

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecylerviewDBAndAdapter() {


//      SETUP RECYCLERVIEW AND ADAPTER
        sopList = roomDatabase().mysopDao().getAllSOPs();
        CategoryRecyclerAdapter categoriesRecyclerAdapter = new CategoryRecyclerAdapter(sopList, this, roomDatabase());
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCategories.setAdapter(categoriesRecyclerAdapter);
    }

    public AppDatabase roomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mysop")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
