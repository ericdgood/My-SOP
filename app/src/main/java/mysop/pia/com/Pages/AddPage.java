package mysop.pia.com.Pages;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.Categories.ShelfRecyclerAdapter;
import mysop.pia.com.ListofHandbooks.ListofHandbooks;
import mysop.pia.com.ListofHandbooks.ListofHandbooksAdapter;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;

public class AddPage extends AppCompatActivity {

    private static final String TAG = "testing";
    @BindView(R.id.textview_add_sop_step_count)
    TextView textviewStepCount;
    @BindView(R.id.edittext_add_step_title)
    EditText ediitTextStepTitle;
    @BindView(R.id.button_add_sop_another_step)
    Button buttonAddAnotherStep;
    @BindView(R.id.button_add_sop_save)
    Button buttonCompleteSOP;
    @BindView(R.id.edittext_add_step_description)
    EditText edittextDescription;
    @BindView(R.id.imageview_add_step_gallery)
    ImageView imageviewGallery;
    @BindView(R.id.imageview_add_step_camera)
    ImageView imageviewCamera;
    @BindView(R.id.imageview_add_step_image_preview)
    ImageView imageviewImagePreview;
    @BindView(R.id.button_add_step_save_edit)
    Button buttonEditStepSave;


    Uri imageUri;
    String image;
    String stepTitle;
    String sopTitle;
    String categoryName;
    int savedBook = 0;
    int stepNumber;
    String stepDescription;
    String bookColor;
    boolean editStep;
    int PERMISSION_GALLERY = 1;
    int PERMISSION_CAMERA = 2;
    String mCameraFileName;
    String[] GALLERY_PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String[] CAMERA_PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_step);
        ButterKnife.bind(this);
//      GETS SOPTITLE FROM ADD SOP INTENT
        sopTitle = getIntent().getStringExtra(getString(R.string.booktitle));
        categoryName = getIntent().getStringExtra(getString(R.string.shelftitle1));
        bookColor = getIntent().getStringExtra(getString(R.string.bookcolor));
        stepNumber = getIntent().getIntExtra(getString(R.string.pagenum), 1);
        savedBook = ListofHandbooksAdapter.savedBook;
        setStepText();
        pickImageFromGallery();
        editStep();
        setStepTitle();

//      DO THIS IF SOP IS COMPLETED
        buttonCompleteSOP.setOnClickListener(v -> {
            if (AddStepToRoomDatabase()) {
                Intent goToNewSOP = new Intent(this, ListofHandbooks.class);
                goToNewSOP.putExtra(getString(R.string.booktitle), sopTitle);
                startActivity(goToNewSOP);
                finish();
            }
        });
//        DO THIS IF ANOTHER STEP IS ADDED
        buttonAddAnotherStep.setOnClickListener((View v) -> {
            if (AddStepToRoomDatabase()) {
                Intent nextStep = new Intent(this, AddPage.class);
                int nextStepNum = stepNumber + 1;
                nextStep.putExtra(getString(R.string.pagenum), nextStepNum);
                nextStep.putExtra(getString(R.string.booktitle), sopTitle);
                nextStep.putExtra(getString(R.string.bookcolor), bookColor);
                nextStep.putExtra(getString(R.string.save), savedBook);
                nextStep.putExtra(getString(R.string.shelftitle1), categoryName);
                startActivity(nextStep);
                finish();
            }
        });
    }

    private void setStepTitle() {
        if (!editStep) {
            setTitle(getString(R.string.addpage));
        } else {
            setTitle(getString(R.string.editpage));
        }
    }

    private boolean AddStepToRoomDatabase() {
        stepTitle = ediitTextStepTitle.getText().toString();
        if (!stepTitle.equals("")) {
            stepDescription = edittextDescription.getText().toString();

            if (imageUri != null) {
                image = imageUri.toString();
            }

//            SAVE STEPS FOR SOP
            StepsRoomData newStep = new StepsRoomData(categoryName ,sopTitle, stepTitle, stepNumber, stepDescription, image, savedBook, bookColor, null, 0);
            if (!editStep) {
                stepsRoomDatabase().listOfSteps().insertSteps(newStep);
                return true;
            } else {
                newStep.setId(ListOfPagesAdapter.stepId);
                stepsRoomDatabase().listOfSteps().updateStep(newStep);
                Toast.makeText(this, R.string.editssaved, Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            Toast.makeText(this, R.string.entertitle, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public StepsAppDatabase stepsRoomDatabase() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public void setStepText() {
        String stepConcat = getString(R.string.stepsspace) + stepNumber;
        textviewStepCount.setText(stepConcat);
    }

    private void editStep() {
//        INFO PASSED FROM STEP ACTIVITY
        editStep = getIntent().getBooleanExtra(getString(R.string.editpage1), false);
        stepTitle = getIntent().getStringExtra(getString(R.string.pagetitle));
        stepDescription = getIntent().getStringExtra(getString(R.string.pagedescrip));
        image = getIntent().getStringExtra(getString(R.string.pagepic));
        sopTitle = getIntent().getStringExtra(getString(R.string.booktitle));
        categoryName = ShelfRecyclerAdapter.categoryName;
        bookColor = ListofHandbooksAdapter.bookColor;

        if (editStep) {
//            DO THIS IF MENU EDIT TEXT WAS SELECTED
            ediitTextStepTitle.setText(stepTitle);
            edittextDescription.setText(stepDescription);
            buttonAddAnotherStep.setVisibility(View.GONE);
            buttonCompleteSOP.setVisibility(View.GONE);
            buttonEditStepSave.setVisibility(View.VISIBLE);
            if (image != null) {
                imageviewImagePreview.setVisibility(View.VISIBLE);
                Glide.with(this).load(image).into(imageviewImagePreview);
            }

            buttonEditStepSave.setOnClickListener(v -> {
                if (AddStepToRoomDatabase()) {
                    Intent goToNewSOP = new Intent(this, ListOfPages.class);
                    goToNewSOP.putExtra(getString(R.string.booktitle), sopTitle);
                    startActivity(goToNewSOP);
                    finish();
                }
            });
        }
    }

    //  THIS CREATES THE IMAGE AS A BUTTON TO OPEN GALLERY OR CAMERA
    public void pickImageFromGallery() {
        imageviewGallery.setOnClickListener(v -> ActivityCompat.requestPermissions(this,
                GALLERY_PERMISSIONS,
                PERMISSION_GALLERY));
        imageviewCamera.setOnClickListener(v -> ActivityCompat.requestPermissions(this,
                CAMERA_PERMISSIONS,
                PERMISSION_CAMERA));
    }

    //    THIS ASKS FOR PERMISSION FOR GALLER AND CAMERA AND CREATES INTENT
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
//            GALLERY PERMISSIONS
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // GALLERY PERMISSION GRANTED. THIS OPENS GALLERY CHOOSER
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType(getString(R.string.image));
                    startActivityForResult(intent, 2);
                } else {
                    // permission denied, boo!
                    Toast.makeText(this, R.string.permisdenied, Toast.LENGTH_SHORT).show();
                }
                return;
            }
//            CAMERA PERMISSIONS
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                    Date date = new Date();
                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("-mm-ss");

                    String newPicFile = df.format(date) + getString(R.string.jpg);
                    String outPath = getString(R.string.sdcard) + newPicFile;
                    File outFile = new File(outPath);

                    mCameraFileName = outFile.toString();
                    Uri outuri = Uri.fromFile(outFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(this, R.string.premisden, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    //    THIS GETS RESULTS FROM GALLERY OR CAMERA INTENT AND SHOWS PREVIEW AND SAVES
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//                RESULTS FROM CAMERA CAPTURE
        if (requestCode == 2) {
            if (data != null) {
                imageviewImagePreview.setVisibility(View.VISIBLE);
                imageUri = data.getData();
                Glide.with(this).load(imageUri).into(imageviewImagePreview);
            }
            if (imageUri == null && mCameraFileName != null) {
                imageviewImagePreview.setVisibility(View.VISIBLE);
                imageUri = Uri.fromFile(new File(mCameraFileName));
                Glide.with(this).load(imageUri).into(imageviewImagePreview);
            }
        }
    }
}