package mysop.pia.com.Steps;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class StepActivity extends AppCompatActivity {


    private static final String TAG = "stepActivity";
    @BindView(R.id.textview_step_title)
    TextView textviewStepTitle;
    @BindView(R.id.imageview_step_picture)
    ImageView imageviewStepPicture;
    @BindView(R.id.textview_step_description)
    TextView textviewStepDescription;
    @BindView(R.id.button_page_prev)
    Button btnPagePrev;
    @BindView(R.id.button_page_next)
    Button btnPageNext;

    List<StepsRoomData> listOfSteps;
    int position;
    int numberOfSteps;
    String stringPicture;
    String stringDescription;
    String stringStepTitle;
    String stringStepNumber;
    String StringSopTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        stringStepTitle = getIntent().getStringExtra("sopTitle");
        position = getIntent().getIntExtra("position", 0);

        listOfSteps = stepsRoomDatabase().listOfSteps().getAllSteps(stringStepTitle);
        numberOfSteps = listOfSteps.size();
        stringStepNumber = String.valueOf(listOfSteps.get(position).getStepNumber());

        setTitleBar();
//        GET AND SET TITLE
        setStepTitle();
//        GET AND SET IMAGE
        pictureVisibility();
//        GET AND SET DESCRIPTION
        descriptionVisibility();
//        nextPrev();
    }

    private void setTitleBar(){
        String stepNumberConCat = "Step " + stringStepNumber + " out of " + numberOfSteps;
        setTitle(stepNumberConCat);
    }

    private void setStepTitle() {
        textviewStepTitle.setText(stringStepTitle);
    }

    private void pictureVisibility(){
        stringPicture = listOfSteps.get(position).getImageURI();
        if (stringPicture != null) {
            Glide.with(this).load(stringPicture).into(imageviewStepPicture);
        } else {
            Glide.with(this).load(R.drawable.mysop_logo).into(imageviewStepPicture);
        }
    }

    private void descriptionVisibility(){
        stringDescription = listOfSteps.get(position).getStepDescription();
        if (!stringDescription.equals("")) {
            textviewStepDescription.setText(stringDescription);
        } else {
            textviewStepDescription.setText("No Description");
        }
    }

    private void nextPrev(){
        int stepNum = Integer.parseInt(stringStepNumber);

        if (stepNum < numberOfSteps){
            btnPageNext.setOnClickListener(v -> {

            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.step_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_step_edit_step) {
            Intent editStep = new Intent(this, AddStep.class);
            editStep.putExtra("editStep", true);
            editStep.putExtra("stepNumber",Integer.valueOf(stringStepNumber));
            editStep.putExtra("stepTitle", stringStepTitle);
            editStep.putExtra("stepDescription", stringDescription);
            editStep.putExtra("editImage", stringPicture);
            editStep.putExtra("sopTitle", StringSopTitle);
            startActivity(editStep);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
