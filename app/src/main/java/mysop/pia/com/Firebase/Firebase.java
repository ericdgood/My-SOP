package mysop.pia.com.Firebase;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import mysop.pia.com.R;
import mysop.pia.com.Steps.StepsRoom.StepsAppDatabase;
import mysop.pia.com.Steps.StepsRoom.StepsRoomData;

public class Firebase extends Activity {


    private static final String TAG = "firebase";
    @BindView(R.id.button_share)
    Button buttonShare;
    @BindView(R.id.edittext_firebase_username)
    EditText etUsername;
    @BindView(R.id.edittext_firebase_email)
    EditText etEmail;

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    @BindView(R.id.edittext_firebase_password)
    EditText etPassword;
    public static String mUsername;
    List<StepsRoomData> sopSteps = new ArrayList<>();
    private String sopTitle;
    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSopStepsDatabaseReference;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase);
        ButterKnife.bind(this);

        sopTitle = getIntent().getStringExtra("sopTitle");
        mUsername = ANONYMOUS;

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("sop");
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
//                    USER IS SIGNED IN
                onSignedInInitialize(user.getDisplayName());
//                mSopStepsDatabaseReference = mFirebaseDatabase.getReference().child("users");
//
//                mSopStepsDatabaseReference.push().setValue(mUsername);
            }
            return;
//            else {
////                    USER IS SIGNED OUT
//                onSignedOutCleanup();
//                startActivityForResult(
//                        AuthUI.getInstance()
//                                .createSignInIntentBuilder()
//                                .setIsSmartLockEnabled(false)
//                                .setAvailableProviders(Arrays.asList(
//                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
//                                        new AuthUI.IdpConfig.EmailBuilder().build()))
//                                .build(),
//                        RC_SIGN_IN);
//            }
        };

        buttonShare.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String username = etUsername.getText().toString();

            com.google.firebase.database.Query userNameQuery = mUsersDatabaseReference.orderByChild("userName").equalTo(username);

            userNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        Toast.makeText(Firebase.this, "Choose a different user name", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i(TAG, "email: " + email + " pass " + password);
                        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Firebase.this, (task) -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Firebase.this, "Sign up Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Map neuUser = new HashMap();
                                neuUser.put("userName", username);

                                mUsersDatabaseReference.push().setValue(neuUser);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//            sopSteps = stepsDB().listOfSteps().getAllSteps(sopTitle);
//            String sopTitleTest = sopSteps.get(0).getSopTitle();
//            mSopStepsDatabaseReference.child(mUsername).push().setValue(sopTitleTest);
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                FirebaseData test = dataSnapshot.getValue(FirebaseData.class);
//                Object fireBaseData = dataSnapshot.getValue();
//                Log.i(TAG, "onChildAdded: " + test);
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
        };
        mSopStepsDatabaseReference.addChildEventListener(mChildEventListener);

//        recyclerAdapter = new ListofSOPsAdapter(this, highScores);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public StepsAppDatabase stepsDB() {
        return Room.databaseBuilder(getApplicationContext(), StepsAppDatabase.class, "steps")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;
//        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
//        mMessageAdapter.clear();
//        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}
