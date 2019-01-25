package mysop.pia.com.Steps;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofSOPs.ListofSOPsAdapter;
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

    FirebaseUser user;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("page_photo");

        StringSopTitle = ListofSOPsAdapter.bookTitle;
        position = getIntent().getIntExtra("position", 0);

        listOfSteps = ListOfSteps.listOfSteps;
        numberOfSteps = listOfSteps.size();
        stringStepNumber = String.valueOf(listOfSteps.get(position).getStepNumber());

        setTitleBar();
//        GET AND SET TITLE
        setStepTitle();
//        GET AND SET IMAGE
        pictureVisibility();
//        GET AND SET DESCRIPTION
        descriptionVisibility();
//        NEXT OR PREVIOUS PAGES
        nextPrev();
    }

    private void setTitleBar() {
        String stepNumberConCat = "Step " + stringStepNumber + " out of " + numberOfSteps;
        setTitle(stepNumberConCat);
    }

    private void setStepTitle() {
        stringStepTitle = listOfSteps.get(position).getStepTitle();
        textviewStepTitle.setText(stringStepTitle);
    }

    private void pictureVisibility() {
        stringPicture = listOfSteps.get(position).getImageURI();
        StorageReference photoRef = mChatPhotosStorageReference.child(Uri.parse(stringPicture).getLastPathSegment());
        photoRef.getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Glide.with(getApplicationContext())
                        .load(task.getResult())
                        .into(imageviewStepPicture);

            } else {
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Firebase id", user.getUid());
            }
        });
    }


//                        if (downloadUrl != null) {
//                            Glide.with(this).load(downloadUrl).into(imageviewStepPicture);
//                        }
//                        if (stringPicture != null) {
//                            Glide.with(this).load(stringPicture).into(imageviewStepPicture);
//                        } else {
//                            Glide.with(this).load(R.drawable.mysop_logo).into(imageviewStepPicture);
//
//                        }


                    private void descriptionVisibility() {
                        stringDescription = listOfSteps.get(position).getStepDescription();
                        if (!stringDescription.equals("")) {
                            textviewStepDescription.setText(stringDescription);
                        } else {
                            textviewStepDescription.setText("No Description");
                        }
                    }

                    private void nextPrev() {
                        int stepNum = Integer.parseInt(stringStepNumber);

                        if (stepNum <= numberOfSteps) {
                            btnPageNext.setOnClickListener(v -> {
                                Intent nextStep = new Intent(this, StepActivity.class);
                                nextStep.putExtra("sopTitle", StringSopTitle);
                                nextStep.putExtra("position", position + 1);
                                startActivity(nextStep);
                                finish();
                            });

                            btnPagePrev.setOnClickListener(v -> {
                                Intent nextStep = new Intent(this, StepActivity.class);
                                nextStep.putExtra("sopTitle", StringSopTitle);
                                nextStep.putExtra("position", position - 1);
                                startActivity(nextStep);
                                finish();
                            });

                            if (stepNum == numberOfSteps) {
                                btnPageNext.setVisibility(View.GONE);
                            }

                            if (stepNum == 1) {
                                btnPagePrev.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public boolean onCreateOptionsMenu(Menu menu) {
                        if (listOfSteps.get(position).getSharedStatus() == 2 || listOfSteps.get(position).getSharedStatus() == 5) {
                            return false;
                        }
                        getMenuInflater().inflate(R.menu.step_menu, menu);
                        return true;
                    }

                    @Override
                    public boolean onOptionsItemSelected(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_step_edit_step) {
                            Intent editStep = new Intent(this, AddStep.class);
                            editStep.putExtra("editStep", true);
                            editStep.putExtra("stepNumber", Integer.valueOf(stringStepNumber));
                            editStep.putExtra("stepTitle", stringStepTitle);
                            editStep.putExtra("stepDescription", stringDescription);
                            editStep.putExtra("editImage", stringPicture);
                            editStep.putExtra("sopTitle", StringSopTitle);
                            startActivity(editStep);
                            finish();
                            return true;
                        }
                        if (id == R.id.menu_step_delete) {
                            alertToDelete();
                            return true;
                        }

                        return super.onOptionsItemSelected(item);
                    }

                    private void alertToDelete() {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
                        builder.setTitle("Delete step")
                                .setMessage("Are you sure you want to delete this step?")
                                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                    // continue with delete
                                    stepsRoomDatabase().listOfSteps().DeletePAGE(stringStepTitle);
                                    stepsRoomDatabase().listOfSteps().updatePageNumber(Integer.parseInt(stringStepNumber), StringSopTitle);
                                    Toast.makeText(this, stringStepTitle + " is Deleted from book", Toast.LENGTH_SHORT).show();
                                    Intent returnToPageList = new Intent(this, ListOfSteps.class);
                                    returnToPageList.putExtra("sopTitle", StringSopTitle);
                                    startActivity(returnToPageList);
                                    finish();
                                })
                                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                                    // do nothing
                                })
                                .setIcon(R.drawable.ic_delete)
                                .show();
//        return position;
                    }

                    public StepsAppDatabase stepsRoomDatabase() {
                        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                                .fallbackToDestructiveMigration()
                                .allowMainThreadQueries()
                                .build();
                    }

                    @Override
                    public void onBackPressed() {
                        super.onBackPressed();
                        Intent returnToListSteps = new Intent(this, ListOfSteps.class);
                        returnToListSteps.putExtra("sopTitle", listOfSteps.get(position).getSopTitle());
                        startActivity(returnToListSteps);
                    }
                }
