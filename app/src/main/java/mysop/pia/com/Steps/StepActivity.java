package mysop.pia.com.Steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;

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

        getPassedStepExtra();
        setTitleBar();
        setStepTitle();
        pictureVisibility();
        descriptionVisibility();

    }

    private void setStepTitle() {
        textviewStepTitle.setText(stringStepTitle);
    }

    private void getPassedStepExtra() {
//        PASSED FROM LIST OF STEPS ADAPTER
        stringPicture = getIntent().getStringExtra("stepImage");
        stringDescription = getIntent().getStringExtra("stepDescription");
        stringStepTitle = getIntent().getStringExtra("stepTitle");
        stringStepNumber = getIntent().getStringExtra("stepNumber");
        StringSopTitle = getIntent().getStringExtra("sopTitle");
    }

    private void pictureVisibility(){
        if (stringPicture != null) {
            Glide.with(this).load(stringPicture).into(imageviewStepPicture);
        } else {
            Glide.with(this).load(R.drawable.mysop_logo).into(imageviewStepPicture);
        }
    }

    private void descriptionVisibility(){
        if (!stringDescription.equals("")) {
            textviewStepDescription.setText(stringDescription);
        } else {
            textviewStepDescription.setText("No Description");
        }
    }

    private void setTitleBar(){
        String stepNumberConCat = "Step " + stringStepNumber + " details";
        setTitle(stepNumberConCat);
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

}
