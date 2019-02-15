package mysop.pia.com.Pages;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.ListofHandbooks.ListofHandbooksAdapter;
import mysop.pia.com.Pages.PagesRoom.StepsAppDatabase;
import mysop.pia.com.Pages.PagesRoom.StepsRoomData;
import mysop.pia.com.R;

import static maes.tech.intentanim.CustomIntent.customType;

public class PageActivity extends AppCompatActivity {


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
    @BindView(R.id.no_image)
    TextView tvNoImage;
    @BindView(R.id.imagefrag_frame)
    FrameLayout frameImageFrage;

    List<StepsRoomData> listOfSteps;
    int position;
    int numberOfSteps;
    String stringPicture;
    String stringDescription;
    String stringStepTitle;
    String stringStepNumber;
    String StringSopTitle;
    int sharedStat;

    private StorageReference mChatPhotosStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child(getString(R.string.pagehoto));

        StringSopTitle = ListofHandbooksAdapter.bookTitle;
        position = getIntent().getIntExtra(getString(R.string.position), 0);

        listOfSteps = ListOfPages.listOfSteps;
        numberOfSteps = listOfSteps.size();
        stringStepNumber = String.valueOf(listOfSteps.get(position).getStepNumber());
        sharedStat = listOfSteps.get(position).getSharedStatus();

        setTitleBar();
//        GET AND SET TITLE
        setStepTitle();
//        GET AND SET IMAGE
        pictureVisibility();
//        GET AND SET DESCRIPTION
        descriptionVisibility();
//        NEXT OR PREVIOUS PAGES
        nextPrev();
//        ZOOM IMAGE IF CLIKCED
        imageZoom();
    }

    private void setTitleBar() {
        String stepNumberConCat = getString(R.string.spacestep) + stringStepNumber + getString(R.string.outof) + numberOfSteps;
        setTitle(stepNumberConCat);
    }

    private void setStepTitle() {
        stringStepTitle = listOfSteps.get(position).getStepTitle();
        textviewStepTitle.setText(stringStepTitle);
    }

    private void pictureVisibility() {
        stringPicture = listOfSteps.get(position).getImageURI();
        if (sharedStat != 0 && stringPicture != null) {
            StorageReference photoRef = mChatPhotosStorageReference.child(Uri.parse(stringPicture).getLastPathSegment());
            photoRef.getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Glide.with(getApplicationContext())
                            .load(task.getResult())
                            .into(imageviewStepPicture);
                }
            });
        } else if (stringPicture != null) {
            Glide.with(this).load(stringPicture).into(imageviewStepPicture);
        } else {
            Glide.with(this).load(R.drawable.handbook).into(imageviewStepPicture);
            tvNoImage.setVisibility(View.VISIBLE);
        }
    }

        private void descriptionVisibility () {
            stringDescription = listOfSteps.get(position).getStepDescription();
            if (!stringDescription.equals("")) {
                textviewStepDescription.setText(stringDescription);
            } else {
                textviewStepDescription.setVisibility(View.GONE);
            }
        }

        private void nextPrev () {
            int stepNum = Integer.parseInt(stringStepNumber);

            if (stepNum <= numberOfSteps) {
                btnPageNext.setOnClickListener(v -> {
                    Intent nextStep = new Intent(this, PageActivity.class);
                    nextStep.putExtra(getString(R.string.booktitle), StringSopTitle);
                    nextStep.putExtra(getString(R.string.position), position + 1);
                    startActivity(nextStep);
                    customType(this,"left-to-right");
                    finish();
                });

                btnPagePrev.setOnClickListener(v -> {
                    Intent nextStep = new Intent(this, PageActivity.class);
                    nextStep.putExtra(getString(R.string.booktitle), StringSopTitle);
                    nextStep.putExtra(getString(R.string.position), position - 1);
                    startActivity(nextStep);
                    customType(this,"right-to-left");
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
        public boolean onCreateOptionsMenu (Menu menu){
            if (sharedStat == 1 || sharedStat == 2 || sharedStat == 4 || sharedStat == 5) {
                return false;
            }
            getMenuInflater().inflate(R.menu.step_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();
            if (id == R.id.menu_step_edit_step) {
                Intent editStep = new Intent(this, AddPage.class);
                editStep.putExtra(getString(R.string.editpage1), true);
                editStep.putExtra(getString(R.string.pagenum), Integer.valueOf(stringStepNumber));
                editStep.putExtra(getString(R.string.pagetitle), stringStepTitle);
                editStep.putExtra(getString(R.string.pagedescrip), stringDescription);
                editStep.putExtra(getString(R.string.pagepic), stringPicture);
                editStep.putExtra(getString(R.string.booktitle), StringSopTitle);
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

        private void alertToDelete () {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle(R.string.deletestep)
                    .setMessage(R.string.rusuredele)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // continue with delete
                        stepsRoomDatabase().listOfSteps().DeletePAGE(stringStepTitle);
                        stepsRoomDatabase().listOfSteps().updatePageNumber(Integer.parseInt(stringStepNumber), StringSopTitle);
                        Toast.makeText(this, stringStepTitle + getString(R.string.isdeletednow), Toast.LENGTH_SHORT).show();
                        Intent returnToPageList = new Intent(this, ListOfPages.class);
                        returnToPageList.putExtra(getString(R.string.booktitle), StringSopTitle);
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

        public StepsAppDatabase stepsRoomDatabase () {
            return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, getString(R.string.steps))
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            Intent returnToListSteps = new Intent(this, ListOfPages.class);
            returnToListSteps.putExtra(getString(R.string.booktitle), listOfSteps.get(position).getSopTitle());
            startActivity(returnToListSteps);
        }

        public void imageZoom(){
        imageviewStepPicture.setOnClickListener(v -> {

            frameImageFrage.setVisibility(View.VISIBLE);
            imageFrag imgfrag = new imageFrag();
            FragmentManager fragmentManager = getSupportFragmentManager();
            imgfrag.getImageZoom(stringPicture);
            fragmentManager.beginTransaction()
                    .replace(R.id.imagefrag_frame, imgfrag)
                    .commit();
        });
        }
    }
