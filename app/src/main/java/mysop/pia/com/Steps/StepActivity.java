package mysop.pia.com.Steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
    @BindView(R.id.fab_step_edit)
    FloatingActionButton fabStepEdit;

    String stringPicture;
    String stringDescription;
    String stringStepTitle;
    String stringStepNumber;

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
        editStep();
    }

    private void setStepTitle() {
        textviewStepTitle.setText(stringStepTitle);
    }

    private void getPassedStepExtra() {
       stringPicture = ListOfStepsAdapter.stepImage;
       stringDescription = ListOfStepsAdapter.stepDescrition;
       stringStepTitle = ListOfStepsAdapter.stepTitle;
       stringStepNumber = ListOfStepsAdapter.stepNumber;
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

    private void editStep(){
        fabStepEdit.setOnClickListener(v -> {
            Intent editStep = new Intent(this, AddStep.class);
            editStep.putExtra("editStep", true);
            editStep.putExtra("stepNumber",Integer.valueOf(stringStepNumber));
            editStep.putExtra("stepTitle", stringStepTitle);
            editStep.putExtra("stepDescription", stringDescription);
            editStep.putExtra("editImage", stringPicture);
            startActivity(editStep);
            finish();
        });
    }
}
